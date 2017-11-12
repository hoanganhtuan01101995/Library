package com.hoanganhtuan01101995.admob.banner;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hoanganhtuan01101995.admob.Admob;

/**
 * Created by HOANG ANH TUAN on 8/1/2017.
 */

public class Banner implements BannerService {

    private AdView mAdView;
    private Activity activity;
    private int bannerId;

    public static class Builder {

        private Banner banner;

        public Builder(Activity activity) {
            banner = new Banner();
        }

        public Builder setmAdView(AdView mAdView) {
            this.banner.mAdView = mAdView;
            return this;
        }

        public Builder setBannerId(int bannerId) {
            this.banner.bannerId = bannerId;
            return this;
        }

        public BannerService builder() {
            this.banner.execute();
            return banner;
        }
    }

    private void execute() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(Admob.testDeviceId)
                .build();
        mAdView.loadAd(adRequest);
    }
}
