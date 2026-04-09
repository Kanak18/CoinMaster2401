package com.coinmasterspinlink.cmspinmaster;

import static com.coinmasterspinlink.cmspinmaster.App.bannerAdId;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowMetrics;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import java.util.Arrays;
import java.util.function.Consumer;

public class AdManager {
    private static final String AD_UNIT_ID = bannerAdId; // "ca-app-pub-3940256099942544/9214589741"; // Replace with your actual Ad Unit ID
    private static final String TEST_DEVICE_HASHED_ID = "ABCDEF012345"; // Replace with your actual test device ID
    private static final String TAG = "AdManager";
    private static AdManager instance; // Singleton instance
    private AdView adView;
    private GoogleMobileAdsConsentManager googleMobileAdsConsentManager;

    private int interstitialCount = 0; // Counter for showing interstitial
    int interstitialShowCount = Integer.parseInt(String.valueOf(App.interstitialShowCount));
    static int showInter = App.showInter;
    static int showReward = (int) App.showReward;

    private static RewardedAd rewardedAd;

    private InterstitialAd interstitialAd;

    public static void updateAdSettings(int showInterValue) {
        showInter = showInterValue;
    }
    public static void updateRewardAdSettings(int showRewardValue) {
        showReward = showRewardValue;
    }
    public static void initializeAds(Activity activity) {
        MobileAds.initialize(activity, initializationStatus -> Log.d(TAG, "AdMob Initialized"));
    }

    // Singleton pattern to get the instance
    public static AdManager getInstance() {
        if (instance == null) {
            instance = new AdManager();
        }
        return instance;
    }

    // Initialize Consent Manager with Activity context
    public void initializeConsentManager(Activity activity) {
        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(activity);
        gatherConsent(activity);  // Pass activity context

        interstitialShowCount = Integer.parseInt(String.valueOf(App.interstitialShowCount));
    }

    private void gatherConsent(Activity activity) {
        googleMobileAdsConsentManager.gatherConsent(
                activity,  // This is now a valid Activity context
                consentError -> {
                    if (consentError != null) {
                        Log.w(TAG, "Consent gathering failed: " + consentError.getMessage());
                    }

                    if (googleMobileAdsConsentManager.canRequestAds()) {
                        // Load ads only if consent allows
                        configureTestDevice(activity);
                    }

                    if (googleMobileAdsConsentManager.isPrivacyOptionsRequired()) {
                        // Handle privacy options
                    }
                }
        );
    }


    // Configure test devices
    public static void configureTestDevice(Context context) {
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder()
                        .setTestDeviceIds(Arrays.asList(TEST_DEVICE_HASHED_ID))
                        .build()
        );
    }

    private int parseSafeInt(String value, int defaultValue) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Log.e("AdManager", "Invalid integer value: " + value, e);
            return defaultValue;
        }
    }
    // Load a banner ad
    public void loadBannerAd(Context context, FrameLayout adContainerView) {

        int showBanner = parseSafeInt(App.showBanner, 0);

        if (showBanner > 0) {
            // Check if banner ad ID is null or empty
            if (App.bannerAdId == null || App.bannerAdId.isEmpty()) {
                Log.w(TAG, "Banner Ad ID is null or empty. Skipping ad load.");
                return;
            }
            
            adView = new AdView(context);
            adView.setAdUnitId(App.bannerAdId);
            adView.setAdSize(getAdSize(context));

            Log.d(TAG, "Attempting to load banner ad with ID: " + App.bannerAdId);

            adContainerView.removeAllViews();
            adContainerView.addView(adView);

            AdRequest adRequest = new AdRequest.Builder().build();

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.d(TAG, "Banner ad loaded successfully.");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    Log.e(TAG, "Failed to load banner ad: " + adError.getMessage());
                }
            });

            adView.loadAd(adRequest);
        } else {
            Log.d(TAG, "Banner ad disabled (showBanner = 0 or null)");
        }
    }


    // Pause and resume methods for the ad view lifecycle
    public void pauseAd() {
        if (adView != null) {
            adView.pause();
        }
    }

    public void resumeAd() {
        if (adView != null) {
            adView.resume();
        }
    }

    public void destroyAd() {
        if (adView != null) {
            adView.destroy();
        }
    }

    // Get the adaptive banner ad size based on the device's screen width
    private AdSize getAdSize(@NonNull Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int adWidthPixels = displayMetrics.widthPixels;

        // Ensure the context is an instance of Activity
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
                adWidthPixels = windowMetrics.getBounds().width();
            }
        } else {
            Log.w(TAG, "Context is not an instance of Activity. Default screen width will be used.");
        }

        float density = displayMetrics.density;
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
    // Load interstitial ad and show it when loaded
    public void loadInterstitialAd(Activity activity) {
        // Check if interstitial ad ID is null or empty
        if (App.interstitialAdId == null || App.interstitialAdId.isEmpty()) {
            Log.w(TAG, "Interstitial Ad ID is null or empty. Skipping ad load.");
            return;
        }
        
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.d(TAG, "Load Interstital: " + App.interstitialAdId);
        InterstitialAd.load(
                activity,
                App.interstitialAdId, // Replace with your actual Ad Unit ID
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        Log.d(TAG, "Interstitial Ad Loaded.");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        interstitialAd = null;
                        Log.e(TAG, "Failed to load interstitial ad: " + loadAdError.getMessage());
                    }
                }
        );
    }

    // Show interstitial ad and reload after it's dismissed
    public void showInterstitialAd(Activity activity) {
        Log.d(TAG, "Show Interstitial Flag: " + showInter);
        if (showInter == 1) { // Check if interstitial ads are enabled
            Log.d(TAG, "Show Counter: " + interstitialCount + " API Count: " + interstitialShowCount);

            // Show the ad when counter is 0 (first time) OR when it reaches the limit
            if (interstitialCount == 0 || interstitialCount >= interstitialShowCount) {
                if (interstitialAd != null) {
                    interstitialAd.show(activity);

                    // Set callback to load a new ad immediately after closing
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Interstitial ad dismissed.");
                            interstitialAd = null; // Clear reference
                            interstitialCount = 1; // Start counter from 1 after first ad
                            loadInterstitialAd(activity); // Load new ad immediately
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            Log.e(TAG, "Failed to show interstitial ad: " + adError.getMessage());
                            loadInterstitialAd(activity); // Load new ad in case of failure
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "Interstitial ad shown.");
                        }
                    });
                } else {
                    Log.d(TAG, "Interstitial ad not ready. Loading a new one.");
                    loadInterstitialAd(activity); // Load ad if it's not available
                }
            } else {
                interstitialCount++; // Increment counter only when ad is not shown
                Log.d(TAG, "Interstitial count: " + interstitialCount + " / " + interstitialShowCount);
            }
        } else {
            Log.d(TAG, "Interstitial ads are disabled.");
        }
    }

    public static void loadRewardedAd(Activity activity) {
        // Check if rewarded ad ID is null or empty
        if (App.rewardAdId == null || App.rewardAdId.isEmpty()) {
            Log.w(TAG, "Rewarded Ad ID is null or empty. Skipping ad load.");
            return;
        }
        
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity, App.rewardAdId, // Replace with your Rewarded Ad ID
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Rewarded Ad Loaded.");
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        rewardedAd = null;
                        Log.e(TAG, "Failed to load rewarded ad: " + adError.getMessage());
                    }
                });
    }
    public static boolean isRewardedAdReady() {
        return rewardedAd != null;
    }
    public static void showRewardedAd(Activity activity, Consumer<RewardItem> onRewardEarned) {

        Log.d(TAG, "Show Interstitial Flag: " + showInter);
        if (showReward == 1) { // Check if interstitial ads are enabled
            if (rewardedAd != null) {
                rewardedAd.show(activity, rewardItem -> {
                    Log.d(TAG, "User Earned Reward: " + rewardItem.getAmount() + " " + rewardItem.getType());
                    onRewardEarned.accept(rewardItem);
                });

                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Rewarded ad dismissed.");
                        rewardedAd = null; // Clear reference
                        loadRewardedAd(activity); // Load a new ad
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        Log.e(TAG, "Failed to show rewarded ad: " + adError.getMessage());
                        loadRewardedAd(activity); // Reload ad if it fails to show
                    }
                });
            } else {
                Log.d(TAG, "Rewarded ad not ready. Loading a new one.");
                loadRewardedAd(activity); // Load a new ad if it's not available
            }
        }
    }

}
