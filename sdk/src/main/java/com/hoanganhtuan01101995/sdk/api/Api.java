package com.hoanganhtuan01101995.sdk.api;

import android.content.Context;

import com.hoanganhtuan01101995.sdk.db.Playlist;
import com.hoanganhtuan01101995.sdk.db.Video;

/**
 * Created by HoangAnhTuan on 8/24/2017.
 */

public class Api {

    private static Api sdk;

    public static void instance(Context context) {
        if (sdk == null) sdk = new Api(context);
    }

    private ApiService service;

    private Api(Context context) {
        service = new ApiImpl(context);
    }

    public static void callApiGetVideosByPlaylistId(String pageToken, String playlistId, ApiCallback<Video> callback) {
        sdk.service.callApiGetVideosByPlaylistId(pageToken, playlistId, callback);
    }

    public static void callApiGetVideosNew(String pageToken, ApiCallback<Video> callback) {
        sdk.service.callApiGetVideosNew(pageToken, callback);
    }

    public static void callApiGetVideosFromSave(ApiCallback<Video> callback) {
        sdk.service.callApiGetVideosFromSave(callback);
    }

    public static void callApiGetVideoFromHistory(ApiCallback<Video> callback) {
        sdk.service.callApiGetVideoFromHistory(callback);
    }

    public static void callApiSearchVideosByName(String s, ApiCallback<Video> callback) {
        sdk.service.callApiSearchVideosByName(s, callback);
    }

    public static void callApiGetPlaylists(String pageToken, ApiCallback<Playlist> callback) {
        sdk.service.callApiGetPlaylists(pageToken, callback);
    }

    public static void writeHistory(Video video) {
        sdk.service.writeHistory(video);
    }

    public static void pinVideo(Video video) {
        sdk.service.pinVideo(video);
    }

    public static void unpinVideo(Video video) {
        sdk.service.unpinVideo(video);
    }

    public static boolean isPin(String videoId) {
        return sdk.service.isPin(videoId);
    }
}
