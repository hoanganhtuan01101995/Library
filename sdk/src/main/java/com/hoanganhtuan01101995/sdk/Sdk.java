package com.hoanganhtuan01101995.sdk;

import android.content.Context;
import android.support.annotation.StringRes;

import com.hoanganhtuan01101995.sdk.api.Api;

/**
 * Created by Hoang Anh Tuan on 11/6/2017.
 */

public class Sdk {

//    public static final int INTERSTITIAL_KEY = R.string.interstitialKey;
//    public static final int REWARDED_VIDEO_KEY = R.string.rewardedVideoKey;
//    public static final String APP_KEY = "AIzaSyAmf_ICIgnjUuSKTFx4A_fTNiH6eeIHVxg";
//    public static final String BROWSER_KEY = "AIzaSyCqpXAb5VH8RdhjUurFtUJ7Z7IpuorQvkE";
//    public static final String CHANNEL_ID = "UCAuUUnT6oDeKwE6v1NGQxug";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Sdk.instance(this, APP_KEY, BROWSER_KEY, INTERSTITIAL_KEY, REWARDED_VIDEO_KEY, CHANNEL_ID);
//    }

    public static int INTERSTITIAL_KEY;
    public static int REWARDED_VIDEO_KEY;
    public static String APP_KEY;
    public static String BROWSER_KEY;
    public static String CHANNEL_ID;

    public static void instance(Context context,
                                String appKey,
                                String browerKey,
                                @StringRes int interstitialKey,
                                @StringRes int rewardedVideoKey,
                                String channelId) {

        INTERSTITIAL_KEY = interstitialKey;
        REWARDED_VIDEO_KEY = rewardedVideoKey;
        APP_KEY = appKey;
        BROWSER_KEY = browerKey;
        CHANNEL_ID = channelId;

        Api.instance(context);
    }
}
