package com.coinmasterspinlink.cmspinmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpinList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SpinAdapter spinAdapter;
    private ProgressBar progressBar; // Add this line

    private AdManager adManager;
    private FrameLayout adContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CoinMaster); // Ensure the new theme is applied
        setContentView(R.layout.activity_spin_list);

        // Set up MaterialToolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpinList.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        adContainerView = findViewById(R.id.ad_view_container);
        adManager = ((App) getApplication()).getAdManager();  // Get the global AdManager instance
        // Load the banner ad (with null check)
        if (App.bannerAdId != null && !App.bannerAdId.isEmpty()) {
            adManager.loadBannerAd(this, adContainerView);
        }
        if (App.interstitialAdId != null && !App.interstitialAdId.isEmpty()) {
            adManager.loadInterstitialAd(this);
        }
        //show interstitial
        adManager.showInterstitialAd(this);



        progressBar = findViewById(R.id.progressBar); // Initialize the ProgressBar
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchRewards();
    }

    private void fetchRewards() {
        progressBar.setVisibility(View.VISIBLE); // Show progress bar while fetching
        RewardRequest request = new RewardRequest("get_rewards", "2000");
        ApiClient.getApiService().getRewards(request).enqueue(new Callback<RewardsResponse>() {
            @Override
            public void onResponse(Call<RewardsResponse> call, Response<RewardsResponse> response) {
                progressBar.setVisibility(View.GONE); // Hide progress bar on response
                Log.d("SpinListActivity", "API Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    List<RewardsResponse.Reward> rewards = response.body().getMsg();
                    Log.d("SpinListActivity", "Rewards size: " + (rewards != null ? rewards.size() : "null"));
                    spinAdapter = new SpinAdapter(SpinList.this, rewards); // Pass context here
                    recyclerView.setAdapter(spinAdapter);
                } else {
                    Log.e("SpinListActivity", "API call failed or empty body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RewardsResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide progress bar on failure
                Log.e("SpinListActivity", "Error fetching rewards", t);
            }
        });
    }
}
