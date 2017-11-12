package com.hoanganhtuan01101995.library;

import android.app.Application;

import com.hoanganhtuan01101995.sdk.Sdk;

/**
 * Created by Hoang Anh Tuan on 11/9/2017.
 */

public class App extends Application {
    public static String IMG_TYPE = "hqdefault";
    public static boolean DEBUG = false;
    public static final int INTERSTITIAL_KEY = R.string.interstitialKey;
    public static final int REWARDED_VIDEO_KEY = R.string.rewardedVideoKey;
    public static final String APP_KEY = "AIzaSyAmf_ICIgnjUuSKTFx4A_fTNiH6eeIHVxg";
    public static final String BROWSER_KEY = "AIzaSyCqpXAb5VH8RdhjUurFtUJ7Z7IpuorQvkE";
    public static final String CHANNEL_ID = "UCAuUUnT6oDeKwE6v1NGQxug";

    @Override
    public void onCreate() {
        super.onCreate();
        Sdk.instance(this, DEBUG, IMG_TYPE, APP_KEY, BROWSER_KEY, INTERSTITIAL_KEY, REWARDED_VIDEO_KEY, CHANNEL_ID);
    }
}
