package com.coinmasterspinlink.cmspinmaster;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.appbar.MaterialToolbar;
import com.bumptech.glide.Glide;

public class SpinRewardActivity extends AppCompatActivity {
    private TextView titleTextView, authorTextView, dateTextView;
    private ImageView authorImageView;
    private FrameLayout adContainerView;
    private AdManager adManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CoinMaster); // Ensure the new theme is applied
        setContentView(R.layout.activity_spin_reward);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up MaterialToolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpinRewardActivity.this, SpinList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        adContainerView = findViewById(R.id.ad_view_container);
        adManager = AdManager.getInstance(); // Get the global AdManager instance
        // Load ads with null checks
        if (App.interstitialAdId != null && !App.interstitialAdId.isEmpty()) {
            adManager.loadInterstitialAd(this);
        }
        if (App.bannerAdId != null && !App.bannerAdId.isEmpty()) {
            adManager.loadBannerAd(this, adContainerView);
        }
        adManager.showInterstitialAd(this);

        Button btnClaim = findViewById(R.id.btnClaim);
        Button btnShare = findViewById(R.id.btnShare);

        titleTextView = findViewById(R.id.titleTextView);
        authorTextView = findViewById(R.id.authorTextView);
        dateTextView = findViewById(R.id.dateTextView);
        authorImageView = findViewById(R.id.authorImageView);

        // Get data from the intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String date = intent.getStringExtra("date");
        String authorPic = intent.getStringExtra("authorPic");
        String link = intent.getStringExtra("link");

        // Set data to views
        titleTextView.setText(title);
        authorTextView.setText(author);
        dateTextView.setText(date);
        
        // Load author image using Glide
        if (authorPic != null && !authorPic.isEmpty()) {
            Glide.with(this)
                .load(authorPic)
                .placeholder(R.drawable.ic_coin)
                .error(R.drawable.ic_coin)
                .into(authorImageView);
        } else {
            authorImageView.setImageResource(R.drawable.ic_coin);
        }

        // Set up button click listeners here
        btnClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link == null || link.isEmpty()) {
                    Toast.makeText(SpinRewardActivity.this, "Link not available", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if showReward is greater than 0 before showing the rewarded ad
                if (App.showReward > 0) {
                    if (AdManager.isRewardedAdReady()) {
                        Log.d("RewardAd", "Ad is ready.");

                        // Show Rewarded Ad
                        AdManager.showRewardedAd(SpinRewardActivity.this, rewardItem -> {
                            // Handle reward logic here
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();

                            Log.d("RewardAd", "User earned: " + rewardAmount + " " + rewardType);
                            Toast.makeText(SpinRewardActivity.this, "You earned: " + rewardAmount + " " + rewardType, Toast.LENGTH_SHORT).show();

                            // Show a popup confirming reward claim
                            showStyledPopup(link);
                        });

                    } else {
                        // If the rewarded ad is not ready, open the link directly
                        openLink(link);
                    }
                } else {
                    // If showReward is 0 or less, open the link directly
                    openLink(link);
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link != null && !link.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                    startActivity(Intent.createChooser(shareIntent, "Share link via"));
                } else {
                    Toast.makeText(SpinRewardActivity.this, "Link not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Set up home icon click action


    }
    private void showStyledPopup(String link) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reward_claim);

        // Set background to be transparent to apply custom styles
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize UI elements
        TextView title = dialog.findViewById(R.id.txtTitle);
        TextView message = dialog.findViewById(R.id.txtMessage);
        Button btnOpen = dialog.findViewById(R.id.btnOpen);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        title.setText("Congratulations!");
        message.setText("You have successfully claimed the reward.");

        // Open Link Button Click
        btnOpen.setOnClickListener(v -> {
            openLink(link);
            dialog.dismiss();
        });

        // Close Button Click
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Apply animation
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        dialog.show();
    }

    /**
     * Show a popup dialog to confirm the reward claim with an option to open the link.
     */
    private void showRewardClaimPopup(String link) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SpinRewardActivity.this);
        builder.setTitle("Reward Claimed!");
        builder.setMessage("You have successfully claimed the reward.");

        builder.setPositiveButton("Open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openLink(link);
            }
        });

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Open the provided link in a browser.
     */
    private void openLink(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
}