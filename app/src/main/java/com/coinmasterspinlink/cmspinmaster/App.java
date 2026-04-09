package com.coinmasterspinlink.cmspinmaster;



import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.onesignal.OneSignal;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {

    private static final String TAG = "App";
    private static SettingsResponse settings;
    private AdManager adManager;

    public static String adNetwork;
    public static String showOpenAdsAdmob;
    public static String showBanner;
    public static String showNative;
    public static String bannerAdId;
    public static String interstitialAdId;
    public static String nativeAdId;
    public static String rewardAdId;
    public static String openAdId;
    public static String appPrivacyPolicy;

    private static final String ONESIGNAL_APP_ID = "95ebebc9-fecd-43de-9143-30d96698cded";


    public static Uri privacyPolicyUrl;
    public static Uri faqUrl;
    public static String app_redirect_url;

    public static String app_update_desc;
    public static boolean cancel_update_status;
    public static Integer app_new_version;
    public static Integer app_update_status;



    static String interstitialShowCount = "10"; // Default value
    static Integer showInter = Integer.valueOf("1"); // Default value
    static Integer showReward = Integer.valueOf("1"); // Default value



    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> Log.d(TAG, "MobileAds initialized"));

        // Initialize AdManager
        adManager = AdManager.getInstance();

        // Fetch settings from the API
        fetchSettings();

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        //OneSignal.promptForPushNotifications();

        //OneSignal.setNotificationOpenedHandler(new MyNotificationHandler(this));

        // Delay asking for permission until after splash screen
        new Handler().postDelayed(() -> OneSignal.promptForPushNotifications(), 4000);

        OneSignal.setNotificationOpenedHandler(new MyNotificationHandler(this));

    }



    private void fetchSettings() {
        SettingRequest request = new SettingRequest("get_settings", "2000"); // Replace with appropriate service_type and app_id
        ApiClient.getApiService().getSettings(request).enqueue(new Callback<SettingsResponse>() {

            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log full response for debugging
                    settings = response.body(); // Store the settings globally

                    Log.d("SettingsAPI", "Full Parsed Response: " + new Gson().toJson(settings));

                    // Check individual fields and log them
                    adNetwork = settings.getAdNetwork();
                    bannerAdId = settings.getBannerAdId();
                    interstitialAdId = settings.getInterstitialAdId();
                    showBanner = settings.getShowBanner();
                   // showInter = settings.getShowInter();
                    interstitialShowCount = String.valueOf(settings.getInterstitialShowCount());


                    showInter = settings.getShowInter() != null ? settings.getShowInter() : 0;
                    Log.d("SettingsAPI", "Updated showInter Value: " + showInter);
                    AdManager.updateAdSettings(showInter);

                    showReward = settings.getShowReward() != null ? Integer.valueOf(settings.getShowReward()) : 0;
                    Log.d("SettingsAPI", "Updated showReward Value: " + showReward);
                    AdManager.updateRewardAdSettings((Integer) showReward);

                    rewardAdId = settings.getRewardAdId();

                    app_update_status = settings.getAppUpdateStatus();
                    app_new_version = settings.getAppNewVersion();
                    app_update_desc = settings.getAppUpdateDesc();
                    app_redirect_url = String.valueOf(Uri.parse(settings.getAppRedirectUrl()));
                    cancel_update_status = settings.getCancelUpdateStatus();


                    Log.d("SettingsAPI", "Full Parsed Response: " + new Gson().toJson(settings));
                    privacyPolicyUrl = Uri.parse(settings.getAppPrivacyPolicy());
                    faqUrl = Uri.parse(settings.getAppFaq());





                } else {
                    try {
                        // Log the error response body if there's an issue
                        String errorJson = response.errorBody() != null
                                ? response.errorBody().string()
                                : "Error body is null";
                        Log.e("SettingsAPI", "Error Response: " + errorJson);
                    } catch (IOException e) {
                        Log.e("SettingsAPI", "Error reading error response body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {
                Log.e("fetchSettings", "Error fetching settings", t);
            }
        });
    }

    // Getter for settings to be accessed anywhere in the application
    public static SettingsResponse getSettings() {
        return settings;
    }

    // Getter for AdManager to be accessed anywhere in the application
    public AdManager getAdManager() {
        return adManager;
    }
}
