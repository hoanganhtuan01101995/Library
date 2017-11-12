package com.hoanganhtuan01101995.sdk;

import android.content.Context;
import android.support.annotation.StringRes;

import com.hoanganhtuan01101995.sdk.api.Api;

/**
 * Created by Hoang Anh Tuan on 11/6/2017.
 */

public class Sdk {

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
