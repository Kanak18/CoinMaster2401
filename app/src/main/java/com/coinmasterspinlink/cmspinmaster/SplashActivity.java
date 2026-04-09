package com.coinmasterspinlink.cmspinmaster;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY_MS = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        // Check internet and proceed with 5 second delay
        if (isInternetAvailable()) {
            proceedWithDelay();
        } else {
            if (!isFinishing()) showInternetDialog();
        }
    }

    // Modern Internet Check (Safe for all versions)
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = cm.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            if (capabilities == null) return false;

            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
        } else {
            // Deprecated but needed for old devices
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
    }

    private void showInternetDialog() {
        if (isFinishing()) return; // Prevent WindowLeaked crash

        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please turn on your internet connection to continue.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    if (isInternetAvailable()) {
                        proceedWithDelay();
                    } else {
                        showInternetDialog();
                    }
                })
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void proceedWithDelay() {
        // Wait 5 seconds then proceed
        new Handler(Looper.getMainLooper()).postDelayed(this::checkSettingsAndProceed, SPLASH_DELAY_MS);
    }

    private void checkSettingsAndProceed() {
        SettingsResponse settings = ((App) getApplicationContext()).getSettings();

        if (settings != null) {
            Log.d("SettingsKanak", "Ad Network: " + settings.getAdNetwork());
            proceedToMainActivity();
        } else {
            Log.d("SettingsKanak", "Settings not yet loaded, retrying...");

            // Use main looper for safety
            new Handler(Looper.getMainLooper()).postDelayed(this::checkSettingsAndProceed, 2000);
        }
    }

    private void proceedToMainActivity() {
        if (isFinishing()) return;

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
