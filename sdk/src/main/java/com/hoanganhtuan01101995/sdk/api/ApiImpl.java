package com.hoanganhtuan01101995.sdk.api;

import android.content.Context;

import com.hoanganhtuan01101995.http.Http;
import com.hoanganhtuan01101995.http.HttpCallBack;
import com.hoanganhtuan01101995.http.HttpMethod;
import com.hoanganhtuan01101995.sdk.Sdk;
import com.hoanganhtuan01101995.sdk.db.DaoMaster;
import com.hoanganhtuan01101995.sdk.db.DaoSession;
import com.hoanganhtuan01101995.sdk.db.History;
import com.hoanganhtuan01101995.sdk.db.HistoryDao;
import com.hoanganhtuan01101995.sdk.db.Playlist;
import com.hoanganhtuan01101995.sdk.db.Video;
import com.hoanganhtuan01101995.sdk.db.VideoDao;

import org.greenrobot.greendao.database.Database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by HOANG ANH TUAN on 8/21/2017.
 */

class ApiImpl implements ApiService {

    private static final String TAG = "ApiImpl";

    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static final String URL_SEARCH = "search";
    private static final String URL_PLAYLIST = "playlists";
    private static final String URL_PLAYLIST_ITEMS = "playlistItems";
    private static final String SNIPPET = "snippet,id";

    private Context context;
    private VideoDao videoDao;
    private HistoryDao historyDao;

    ApiImpl(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "data-db");
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();

        videoDao = daoSession.getVideoDao();
        historyDao = daoSession.getHistoryDao();

    }

    @Override
    public void callApiGetVideosByPlaylistId(String pageToken, String playlistId, final ApiCallback<Video> callback) {
        Http.build()
                .setBase_url(BASE_URL)
                .setUrl(URL_PLAYLIST_ITEMS)
                .setMethod(HttpMethod.GET)
                .putParameter("key", Sdk.BROWSER_KEY)
                .putParameter("maxResults", 50 + "")
                .putParameter("part", SNIPPET)
                .putParameter("pageToken", pageToken)
                .putParameter("playlistId", playlistId)
                .withCallBack(new HttpCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String nextPageToken = object.optString("nextPageToken");
                            callback.onNextPageToken(nextPageToken);

                            JSONArray jsonArray = object.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                try {
                                    callback.onProgressUpdate(convertStringJsonPlaylistToVideo(jsonObject));
                                } catch (Exception e) {
                                    callback.onProgressUpdate(new Video(""));
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        callback.onComplete();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        callback.onComplete();
                    }
                })
                .execute();
    }

    @Override
    public void callApiGetVideosNew(String pageToken, final ApiCallback<Video> callback) {
        Http.build()
                .setBase_url(BASE_URL)
                .setUrl(URL_SEARCH)
                .setMethod(HttpMethod.GET)
                .putParameter("key", Sdk.BROWSER_KEY)
                .putParameter("channelId", Sdk.CHANNEL_ID)
                .putParameter("part", SNIPPET)
                .putParameter("order", "date")
                .putParameter("maxResults", 50 + "")
                .putParameter("pageToken", pageToken)
                .withCallBack(new HttpCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String nextPageToken = object.optString("nextPageToken");
                            callback.onNextPageToken(nextPageToken);
                            JSONArray jsonArray = object.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                try {
                                    callback.onProgressUpdate(convertStringJsonToVideo(jsonObject));
                                } catch (Exception e) {
                                    callback.onProgressUpdate(new Video(""));
                                }
                            }
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                        callback.onComplete();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        callback.onComplete();
                    }
                })
                .execute();
    }

    @Override
    public void callApiGetPlaylists(String pageToken, final ApiCallback<Playlist> callback) {
        Http.build()
                .setBase_url(BASE_URL)
                .setUrl(URL_PLAYLIST)
                .setMethod(HttpMethod.GET)
                .putParameter("key", Sdk.BROWSER_KEY)
                .putParameter("channelId", Sdk.CHANNEL_ID)
                .putParameter("maxResults", 50 + "")
                .putParameter("part", SNIPPET)
                .putParameter("pageToken", pageToken)
                .withCallBack(new HttpCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String nextPageToken = object.optString("nextPageToken");
                            callback.onNextPageToken(nextPageToken);

                            JSONArray jsonArray = object.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                callback.onProgressUpdate(convertStringJsonToPlaylist(jsonObject));
                            }
                        } catch (Exception ignored) {
                        }
                        callback.onComplete();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        callback.onComplete();
                    }
                })
                .execute();
    }

    @Override
    public void callApiSearchVideosByName(String s, final ApiCallback<Video> callback) {
        Http.build()
                .setBase_url(BASE_URL)
                .setUrl(URL_SEARCH)
                .setMethod(HttpMethod.GET)
                .putParameter("key", Sdk.BROWSER_KEY)
                .putParameter("channelId", Sdk.CHANNEL_ID)
                .putParameter("type", "video")
                .putParameter("q", s)
                .putParameter("maxResults", 5 + "")
                .putParameter("part", SNIPPET)
                .withCallBack(new HttpCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONArray jsonArray = object.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                try {
                                    callback.onProgressUpdate(convertStringJsonToVideo(jsonObject));
                                } catch (Exception ignored) {
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        callback.onComplete();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        callback.onComplete();
                    }
                })
                .execute();
    }

    @Override
    public void callApiGetVideosFromSave(ApiCallback<Video> callback) {
        List<Video> videos = videoDao.queryBuilder()
                .list();
        for (int i = videos.size() - 1; i >= 0; i--) {
            callback.onProgressUpdate(videos.get(i));
        }
        callback.onComplete();
    }

    @Override
    public void pinVideo(Video video) {
        videoDao.insert(video);
    }

    @Override
    public void unpinVideo(Video video) {
        videoDao.delete(video);
    }

    @Override
    public boolean isPin(String videoId) {
        List<Video> videos = videoDao.queryBuilder()
                .where(VideoDao.Properties.VideoId.eq(videoId))
                .list();
        return videos.size() > 0;
    }

    @Override
    public void callApiGetVideoFromHistory(ApiCallback<Video> callback) {
        List<History> videos = historyDao.queryBuilder()
                .list();
        for (int i = videos.size() - 1; i >= 0; i--) {
            Video video = new Video();
            video.setVideoId(videos.get(i).getVideoId());
            video.setTitle(videos.get(i).getTitle());
            video.setDescription(videos.get(i).getDescription());
            video.setPublishedAt(videos.get(i).getPublishedAt());
            callback.onProgressUpdate(video);
        }
        callback.onComplete();
    }

    @Override
    public void writeHistory(Video video) {
        List<History> videos = historyDao.queryBuilder()
                .where(HistoryDao.Properties.VideoId.eq(video.getVideoId()))
                .list();

        History history = new History();
        history.setVideoId(video.getVideoId());
        history.setTitle(video.getTitle());
        history.setDescription(video.getDescription());
        history.setPublishedAt(video.getPublishedAt());

        if (videos.size() > 0) {
            historyDao.delete(history);
        }
        historyDao.insert(history);
    }

    private Video convertStringJsonToVideo(JSONObject jsonObject) throws JSONException, ParseException {
        Video video = new Video();

        video.setVideoId(jsonObject.getJSONObject("id").getString("videoId"));
        video.setTitle(jsonObject.getJSONObject("snippet").getString("title"));
        video.setDescription(jsonObject.getJSONObject("snippet").getString("description"));
        video.setPublishedAt(convertStringTimeToLong(jsonObject.getJSONObject("snippet").getString("publishedAt")));

        return video;
    }

    private Video convertStringJsonPlaylistToVideo(JSONObject jsonObject) throws JSONException, ParseException {
        Video video = new Video();

        video.setVideoId(jsonObject.getJSONObject("snippet").getJSONObject("resourceId").getString("videoId"));
        video.setTitle(jsonObject.getJSONObject("snippet").getString("title"));
        video.setDescription(jsonObject.getJSONObject("snippet").getString("description"));
        video.setPublishedAt(convertStringTimeToLong(jsonObject.getJSONObject("snippet").getString("publishedAt")));

        return video;
    }

    private Playlist convertStringJsonToPlaylist(JSONObject jsonObject) throws JSONException, ParseException {
        Playlist playlist = new Playlist();

        playlist.setTitle(jsonObject.getJSONObject("snippet").getString("title"));
        playlist.setDescription(jsonObject.getJSONObject("snippet").getString("description"));
        playlist.setPublishedAt(convertStringTimeToLong(jsonObject.getJSONObject("snippet").getString("publishedAt")));
        playlist.setId(jsonObject.getString("id"));
        playlist.setMedium(jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url"));
        playlist.setSelect(false);

        return playlist;
    }

    private long convertStringTimeToLong(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date timeCreatedDate = dateFormat.parse(time);
        return timeCreatedDate.getTime();
    }

}
