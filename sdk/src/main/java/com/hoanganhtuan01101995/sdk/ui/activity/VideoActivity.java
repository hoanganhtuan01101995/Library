package com.hoanganhtuan01101995.sdk.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoanganhtuan01101995.loadmore.EndlessRecyclerOnScrollListener;
import com.hoanganhtuan01101995.loadmore.HeaderAndFooterRecyclerViewAdapter;
import com.hoanganhtuan01101995.loadmore.RecyclerViewUtils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hoanganhtuan01101995.sdk.R;
import com.hoanganhtuan01101995.sdk.Sdk;
import com.hoanganhtuan01101995.sdk.api.Api;
import com.hoanganhtuan01101995.sdk.api.ApiCallback;
import com.hoanganhtuan01101995.sdk.db.Video;
import com.hoanganhtuan01101995.sdk.db.VideoType;
import com.hoanganhtuan01101995.sdk.ui.adapter.NormalAdapter;
import com.hoanganhtuan01101995.sdk.ui.adapter.OnItemVideoClickedListener;
import com.hoanganhtuan01101995.sdk.utils.Utils;
import com.hoanganhtuan01101995.loadmore.view.LoadingFooter;
import com.hoanganhtuan01101995.loadmore.view.RecyclerViewStateUtils;
import com.hoanganhtuan01101995.sdk.view.StateView;

/**
 * Created by HoangAnhTuan on 8/25/2017.
 */

public class VideoActivity extends YouTubeBaseActivity implements
        ApiCallback<Video>,
        OnItemVideoClickedListener,
        YouTubePlayer.OnInitializedListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String PLAYLIST_ID = "playlistId";

    YouTubePlayerView playerView;
    StateView state;
    RecyclerView list;
    SwipeRefreshLayout refresh;


    public static void start(Context context, Video video, VideoType videoType, String playlistId) {
        Intent starter = new Intent(context, VideoActivity.class);
        starter.putExtra(Video.class.getSimpleName(), video);
        starter.putExtra(VideoType.class.getSimpleName(), videoType);
        starter.putExtra(PLAYLIST_ID, playlistId);
        context.startActivity(starter);
    }

    private ViewHolder viewHolder;

    private Video video;
    private VideoType videoType;
    private YouTubePlayer youTubePlayer;

    private NormalAdapter normalAdapter;

    private String pageToken = "";
    private String playlistId = "";
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
    }

    private void initView() {
        playerView = findViewById(R.id.playerView);
        state = findViewById(R.id.state);
        list = findViewById(R.id.list);
        refresh = findViewById(R.id.refresh);

        video = (Video) getIntent().getSerializableExtra(Video.class.getSimpleName());
        videoType = (VideoType) getIntent().getSerializableExtra(VideoType.class.getSimpleName());
        playlistId = getIntent().getStringExtra(PLAYLIST_ID);

        normalAdapter = new NormalAdapter(this);
        normalAdapter.setOnItemVideoClickedListener(this);

        list.setAdapter(new HeaderAndFooterRecyclerViewAdapter(normalAdapter));
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addOnScrollListener(onScrollListener);

        RecyclerViewUtils.setHeaderView(list, createHeader());
        showInfo();

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
                getData();
            }
        });

        playerView.initialize(Sdk.APP_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            this.youTubePlayer = youTubePlayer;
            this.youTubePlayer.loadVideo(video.getVideoId());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    }


    private View createHeader() {
        View view = getLayoutInflater().inflate(R.layout.layout_info_video, null);
        viewHolder = new ViewHolder(view);
        viewHolder.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewHolder.ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Api.isPin(video.getVideoId())) {
                    viewHolder.ivSave.setColorFilter(ContextCompat.getColor(VideoActivity.this, R.color.colorNormal));
                    Api.unpinVideo(video);
                } else {
                    viewHolder.ivSave.setColorFilter(ContextCompat.getColor(VideoActivity.this, R.color.colorBlue));
                    Api.pinVideo(video);
                }
            }
        });
        viewHolder.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.share(VideoActivity.this, "https://www.youtube.com/watch?v=" + video.getVideoId());
            }
        });
        return view;
    }

    private void showInfo() {
        Api.writeHistory(video);
        viewHolder.tvTitle.setText(video.getTitle());
        viewHolder.tvDescription.setText(video.getDescription());
        viewHolder.tvTime.setText(Utils.getTimeAgo(video.getPublishedAt(), this));

        if (Api.isPin(video.getVideoId())) {
            viewHolder.ivSave.setColorFilter(ContextCompat.getColor(VideoActivity.this, R.color.colorBlue));
        } else {
            viewHolder.ivSave.setColorFilter(ContextCompat.getColor(VideoActivity.this, R.color.colorNormal));
        }
    }

    private void getData() {
        switch (videoType) {
            case HOME:
                Api.callApiGetVideosNew(pageToken, this);
                break;
            case PLAYLIST:
                Api.callApiGetVideosByPlaylistId(pageToken, playlistId, this);
                break;
            case SAVE:
                Api.callApiGetVideosFromSave(this);
                break;
            case HISTORY:
                Api.callApiGetVideoFromHistory(this);
                break;
        }
    }

    private EndlessRecyclerOnScrollListener onScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(list);
            if (state == LoadingFooter.State.Loading) return;
            if (count < 50) {
                RecyclerViewStateUtils.setFooterViewState(VideoActivity.this, list, 50, LoadingFooter.State.TheEnd, null);
            } else {
                RecyclerViewStateUtils.setFooterViewState(VideoActivity.this, list, 50, LoadingFooter.State.Loading, null);
                switch (videoType) {
                    case HOME:
                        getData();
                        break;
                    case PLAYLIST:
                        getData();
                        break;
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        pageToken = "";
        normalAdapter.clear();

        list.setAdapter(new HeaderAndFooterRecyclerViewAdapter(normalAdapter));
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addOnScrollListener(onScrollListener);

        RecyclerViewUtils.setHeaderView(list, createHeader());
        showInfo();

        getData();
    }

    @Override
    public void onNextPageToken(String nextPageToken) {
        pageToken = nextPageToken;
    }

    @Override
    public void onProgressUpdate(Video video) {
        count++;
        if (video.getVideoId().isEmpty()) return;
        normalAdapter.add(video);
    }

    @Override
    public void onComplete() {
        RecyclerViewStateUtils.setFooterViewState(list, LoadingFooter.State.Normal);
        if (normalAdapter.getItemCount() > 0) {
            state.changeState(StateView.State.NORMAL);
        } else if (!Utils.online(this)) {
            state.changeState(StateView.State.NO_NETWORK);
        } else {
            state.changeState(StateView.State.NO_DATA);
        }
        if (refresh.isRefreshing()) refresh.setRefreshing(false);
    }

    @Override
    public void onItemVideoClicked(Video video) {
        this.video = video;
        youTubePlayer.loadVideo(video.getVideoId());
        showInfo();
    }

    static class ViewHolder {
        ImageView ivSave;
        ImageView ivBack;
        ImageView ivSend;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvTime;

        ViewHolder(View view) {
            ivSave = view.findViewById(R.id.ivSave);
            ivBack = view.findViewById(R.id.ivBack);
            ivSend = view.findViewById(R.id.ivSend);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvTime = view.findViewById(R.id.tvTime);
        }
    }
}
