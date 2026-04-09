package com.coinmasterspinlink.cmspinmaster;



import static com.coinmasterspinlink.cmspinmaster.App.privacyPolicyUrl;
import static com.coinmasterspinlink.cmspinmaster.R.id.ltSpin;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {
    private FrameLayout adContainerView;
    private AdManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CoinMaster); // Ensure the new theme is applied
        setContentView(R.layout.activity_main);

        // Set up MaterialToolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // Extract colors from the SPIN LINK icon and apply them
        applyIconBasedTheme();

        adContainerView = findViewById(R.id.ad_view_container);

        adManager = AdManager.getInstance(); // Get the global AdManager instance
        // Initialize Consent Manager with Activity context
        adManager.initializeConsentManager(this); // Pass Activity context
        
        // Only load ads if settings have been fetched and ad IDs are available
        if (App.bannerAdId != null && !App.bannerAdId.isEmpty()) {
            adManager.loadBannerAd(this, adContainerView);
        }
        if (App.interstitialAdId != null && !App.interstitialAdId.isEmpty()) {
            adManager.loadInterstitialAd(this);
        }
        if (App.rewardAdId != null && !App.rewardAdId.isEmpty()) {
            adManager.loadRewardedAd(this);
        }
        //show interstitial
        adManager.showInterstitialAd(this);


        if (App.app_update_status  > 0 && App.app_new_version > BuildConfig.VERSION_CODE)
        {
            Log.d("UPDATE_STATUS"," Current Version  : "+BuildConfig.VERSION_CODE);
            showAppDialog(App.app_update_desc,App.app_redirect_url,App.cancel_update_status);
        }

//        Toast.makeText(this," Setting AdProvider "+settings.getAdNetwork(),Toast.LENGTH_LONG);

        ImageView spinButton = findViewById(R.id.spinButton);
        ImageView coinButton = findViewById(R.id.coinButton);
        Button faqButton = findViewById(R.id.faqGuide);
        ImageView shareButton = findViewById(R.id.shareButton);
        ImageView rateButton = findViewById(R.id.rateButton);
        ImageView privacyPolicyButton = findViewById(R.id.privacyPolicyButton);

        // Update these to MaterialCardView to match the new layout
        MaterialCardView ltSpin = findViewById(R.id.ltSpin);
        MaterialCardView ltCoin = findViewById(R.id.ltCoin);
        MaterialCardView LayPolicy = findViewById(R.id.LayPolicy);

        ltSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // If ad is not ready, navigate immediately
                LoaderUtil.showLoader(MainActivity.this, "Loading...");
                Intent intent = new Intent(MainActivity.this, SpinList.class);
                startActivity(intent);
                LoaderUtil.hideLoader(); // Hide the loader after starting the activity
                //}
            }
        });


        ltCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoaderUtil.showLoader(MainActivity.this, "Loading...");
                Intent intent = new Intent(MainActivity.this, SpinList.class);
                startActivity(intent);
                LoaderUtil.hideLoader(); // Hide the loader after starting the activity
            }
        });


        // Set up button click listeners here
       /* spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoaderUtil.showLoader(MainActivity.this, "Loading...");
                Intent intent = new Intent(MainActivity.this, SpinList.class);
                startActivity(intent);
                LoaderUtil.hideLoader(); // Hide the loader after starting the activity
            }
        });

        coinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoaderUtil.showLoader(MainActivity.this, "Loading...");
                Intent intent = new Intent(MainActivity.this, SpinList.class);
                startActivity(intent);
                LoaderUtil.hideLoader(); // Hide the loader after starting the activity
            }
        });*/

        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the loader
                LoaderUtil.showLoader(MainActivity.this, "Loading...");

                // Start the new activity
                Intent intent = new Intent(MainActivity.this, cm_guide.class);
                startActivity(intent);

                // Hide the loader after the transition (or handle it in the target activity)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoaderUtil.hideLoader();
                    }
                }, 1000); // Adjust delay if needed
            }
        });

        /*tipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle tip button click
            }
        });*/

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Share the app link
                String appPackageName = getPackageName(); // Ret

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareMessage = "Check out this amazing app: https://play.google.com/store/apps/details?id="+appPackageName; // Replace with your app's Play Store link
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the app's Play Store page for rating
                String appPackageName = getPackageName(); // Retrieves current app's package name
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException e) {
                    // Fallback to Play Store website if Play Store app is not available
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        LayPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the privacy policy link in a browser
                //Toast.makeText(MainActivity.this, "Privacy policy URL is not available"+privacyPolicyUrl, Toast.LENGTH_SHORT).show();
                if (privacyPolicyUrl != null ) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(App.privacyPolicyUrl)));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Privacy policy URL is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Apply theme colors based on the SPIN LINK icon
     */
    private void applyIconBasedTheme() {
        // Extract colors from the app icon
        ColorExtractor.extractColorsFromIcon(this, R.drawable.ic_app_logo, new ColorExtractor.ColorExtractionCallback() {
            @Override
            public void onColorsExtracted(int primaryColor, int secondaryColor, int accentColor) {
                Log.d("ThemeColors", "Extracted colors - Primary: " + String.format("#%06X", (0xFFFFFF & primaryColor)) +
                        ", Secondary: " + String.format("#%06X", (0xFFFFFF & secondaryColor)) +
                        ", Accent: " + String.format("#%06X", (0xFFFFFF & accentColor)));
                
                // You can dynamically apply these colors here if needed
                // For now, we're using the predefined rich theme colors
              //  Toast.makeText(MainActivity.this, "Rich SPIN LINK theme applied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAppDialog(String description, String link, boolean isCancel) {

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_app);
        dialog.setCancelable(false);

        dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textViewDes = dialog.findViewById(R.id.textView_description_dialog_update);
        TextView buttonUpdate = dialog.findViewById(R.id.button_update_dialog_update);
        TextView buttonCancel = dialog.findViewById(R.id.button_cancel_dialog_update);

        if (isCancel) {
            buttonCancel.setVisibility(View.VISIBLE);
        } else {
            buttonCancel.setVisibility(View.GONE);
        }
        textViewDes.setText(description);

        buttonUpdate.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            dialog.dismiss();
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    public void onBackPressed() {
        showExitRateDialog();
    }

    private void showExitRateDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate_exit);
        dialog.setCancelable(true);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }

        Button btnRate = dialog.findViewById(R.id.btnRate);
        Button btnExit = dialog.findViewById(R.id.btnExit);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        btnRate.setOnClickListener(v -> {
            rateApp();
            dialog.dismiss();
        });

        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            finishAffinity(); // Closes entire app
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void rateApp() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        adManager.pauseAd();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adManager.resumeAd();
    }

    @Override
    protected void onDestroy() {
        adManager.destroyAd();
        super.onDestroy();
    }
}
