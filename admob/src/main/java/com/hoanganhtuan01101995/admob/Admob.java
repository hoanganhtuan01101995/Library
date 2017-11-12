package com.hoanganhtuan01101995.admob;

import android.app.Activity;

import com.hoanganhtuan01101995.admob.banner.Banner;
import com.hoanganhtuan01101995.admob.interstitial.InterstitialAd;
import com.hoanganhtuan01101995.admob.video.VideoAd;

/**
 * Created by HOANG ANH TUAN on 8/1/2017.
 */

public class Admob {
    public static String testDeviceId = "";
    private Activity activity;

    public static Admob with(Activity activity) {
        return new Admob(activity);
    }

    private Admob(Activity activity) {
        this.activity = activity;
    }

    public InterstitialAd.Builder asInterstitialAd() {
        return new InterstitialAd.Builder(activity);
    }

    public Banner.Builder asBanner() {
        return new Banner.Builder(activity);
    }

    public VideoAd.Builder asVideo() {
        return new VideoAd.Builder(activity);
    }

}
