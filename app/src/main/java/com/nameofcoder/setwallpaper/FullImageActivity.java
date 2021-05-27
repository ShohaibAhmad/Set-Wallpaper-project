package com.nameofcoder.setwallpaper;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class FullImageActivity<adRequest> extends AppCompatActivity {
        private ImageView fullImage;
        private Button apply;
        private RewardedAd mRewardedAd;
        private AdView adView;
        private final String TAG = "GoogleAds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        adView = findViewById(R.id.adView);
       //ads video start from her
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-5496730255348136/7132332409",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
       // ads video end from her
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NotNull InitializationStatus initializationStatus) {
            }
        });

        bannerAd();
        fullImage = findViewById(R.id.fullImage);
        apply = findViewById(R.id.apply);
        Glide.with(this).load(getIntent().getStringExtra("image")).into(fullImage);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FullImageActivity.this, "Image set successfully", Toast.LENGTH_SHORT).show();
                try {
                    setBackground();
                } catch (IOException e) {
                    Toast.makeText(FullImageActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                // ads condition start
                if (mRewardedAd != null) {
                    Activity activityContext = FullImageActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
                // ads condition end
            }
        });
    }


    private void bannerAd() {
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Log.d(TAG,"onAdClosed: ");
            }

            @Override
            public void onAdOpened() {
                Log.d(TAG,"onAdOpened: ");
            }

            @Override
            public void onAdLoaded() {
                Log.d(TAG,"onAdLoaded: ");

            }

            @Override
            public void onAdClicked() {
                Log.d(TAG,"onAdClicked: ");

            }

            @Override
            public void onAdImpression() {
                Log.d(TAG,"onAdImpression: ");

            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                Log.d(TAG,"onAdFailedToLoad"+loadAdError.getMessage());
            }
        });
    }

    private void setBackground() throws IOException {
        Bitmap bitmap = ((BitmapDrawable)fullImage.getDrawable()).getBitmap();
        WallpaperManager manger = WallpaperManager.getInstance(getApplicationContext());
        manger.setBitmap(bitmap);
    }
}