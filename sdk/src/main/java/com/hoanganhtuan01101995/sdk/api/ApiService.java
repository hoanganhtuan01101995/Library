package com.hoanganhtuan01101995.sdk.api;

import com.hoanganhtuan01101995.sdk.db.Playlist;
import com.hoanganhtuan01101995.sdk.db.Video;

/**
 * Created by HoangAnhTuan on 8/24/2017.
 */

interface ApiService {
    void callApiGetVideosByPlaylistId(String pageToken, String playlistId, ApiCallback<Video> callback);

    void callApiGetVideosNew(String pageToken, ApiCallback<Video> callback);

    void callApiGetPlaylists(String pageToken, ApiCallback<Playlist> callback);

    void callApiSearchVideosByName(String s, ApiCallback<Video> callback);

    void callApiGetVideosFromSave(ApiCallback<Video> callback);

    void callApiGetVideoFromHistory(ApiCallback<Video> callback);

    void pinVideo(Video video);

    void unpinVideo(Video video);

    void writeHistory(Video video);

    boolean isPin(String videoId);
}
