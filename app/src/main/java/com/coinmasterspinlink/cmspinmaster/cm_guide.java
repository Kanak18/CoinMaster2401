package com.coinmasterspinlink.cmspinmaster;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class cm_guide extends AppCompatActivity {
    private WebView webView;
    private Uri FaqUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CoinMaster); // Ensure the new theme is applied
        setContentView(R.layout.activity_cm_guide);

        // Set up MaterialToolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        webView = findViewById(R.id.webView);

        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        FaqUrl = Uri.parse(String.valueOf(App.faqUrl));
        // Load content into the WebView
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(String.valueOf(FaqUrl));
    }
}