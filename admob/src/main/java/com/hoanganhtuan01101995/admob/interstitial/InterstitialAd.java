package com.hoanganhtuan01101995.admob.interstitial;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.hoanganhtuan01101995.admob.Admob;

/**
 * Created by HOANG ANH TUAN on 8/1/2017.
 */

public class InterstitialAd implements InterstitialAdServicer {

    private Activity activity;
    private AdListener adListener;
    private int interstitialAdId;

    private com.google.android.gms.ads.InterstitialAd interstitialAd;

    public static class Builder {

        InterstitialAd interstitialAdImpl;

        public Builder(Activity activity) {
            interstitialAdImpl = new InterstitialAd();
            this.interstitialAdImpl.activity = activity;
        }

        public Builder setAdListener(AdListener adListener) {
            this.interstitialAdImpl.adListener = adListener;
            return this;
        }

        public Builder setInterstitialAdId(@StringRes int interstitialAdId) {
            this.interstitialAdImpl.interstitialAdId = interstitialAdId;
            return this;
        }

        public InterstitialAdServicer builder() {
            this.interstitialAdImpl.execute();
            return interstitialAdImpl;
        }
    }

    private void execute() {
        interstitialAd = new com.google.android.gms.ads.InterstitialAd(activity);
        interstitialAd.setAdUnitId(activity.getString(interstitialAdId));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(Admob.testDeviceId)
                .build();
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (adListener != null) adListener.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (adListener != null) adListener.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                if (adListener != null) adListener.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                if (adListener != null) adListener.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (adListener != null) adListener.onAdLoaded();
            }
        });
    }

    @Override
    public void show() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }
}
