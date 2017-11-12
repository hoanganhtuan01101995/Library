package com.hoanganhtuan01101995.admob.video;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by Hoang Anh Tuan on 11/10/2017.
 */

public class VideoAd implements VideoAdService {
    private RewardedVideoAdListener rewardedVideoAdListener;
    private Activity activity;
    private int videoAdId;

    public static class Builder {

        VideoAd videoAd;

        public Builder(Activity activity) {
            videoAd = new VideoAd();
            this.videoAd.activity = activity;
        }

        public VideoAd.Builder setRewardedVideoAdListener(RewardedVideoAdListener rewardedVideoAdListener) {
            this.videoAd.rewardedVideoAdListener = rewardedVideoAdListener;
            return this;
        }

        public VideoAd.Builder setVideoAdId(@StringRes int videoAdId) {
            this.videoAd.videoAdId = videoAdId;
            return this;
        }

        public VideoAdService builder() {
            this.videoAd.execute();
            return videoAd;
        }
    }

    private RewardedVideoAd rewardedVideoAd;

    private void execute() {
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        rewardedVideoAd.loadAd(activity.getString(videoAdId), new AdRequest.Builder().build());
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                if (rewardedVideoAdListener != null)
                    rewardedVideoAdListener.onRewardedVideoAdLoaded();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                if (rewardedVideoAdListener != null)
                    rewardedVideoAdListener.onRewardedVideoAdOpened();
            }

            @Override
            public void onRewardedVideoStarted() {
                if (rewardedVideoAdListener != null)
                    rewardedVideoAdListener.onRewardedVideoStarted();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                if (rewardedVideoAdListener != null)
                    rewardedVideoAdListener.onRewardedVideoAdClosed();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                if (rewardedVideoAdListener != null)
                    rewardedVideoAdListener.onRewarded(rewardItem);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                if (rewardedVideoAdListener != null)
                    rewardedVideoAdListener.onRewardedVideoAdLeftApplication();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                if (rewardedVideoAdListener != null)
                    rewardedVideoAdListener.onRewardedVideoAdFailedToLoad(i);
            }
        });

    }

    @Override
    public void resume(Activity activity) {
        rewardedVideoAd.resume(activity);
    }

    @Override
    public void pause(Activity activity) {
        rewardedVideoAd.pause(activity);
    }

    @Override
    public void destroy(Activity activity) {
        rewardedVideoAd.destroy(activity);
    }

    @Override
    public void show() {
        rewardedVideoAd.show();
    }
}
