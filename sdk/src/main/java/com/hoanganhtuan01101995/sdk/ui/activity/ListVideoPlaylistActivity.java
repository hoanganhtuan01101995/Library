package com.hoanganhtuan01101995.sdk.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoanganhtuan01101995.loadmore.EndlessRecyclerOnScrollListener;
import com.hoanganhtuan01101995.loadmore.HeaderAndFooterRecyclerViewAdapter;
import com.hoanganhtuan01101995.sdk.R;
import com.hoanganhtuan01101995.sdk.api.Api;
import com.hoanganhtuan01101995.sdk.api.ApiCallback;
import com.hoanganhtuan01101995.sdk.db.Video;
import com.hoanganhtuan01101995.sdk.db.VideoType;
import com.hoanganhtuan01101995.sdk.ui.adapter.NormalAdapter;
import com.hoanganhtuan01101995.sdk.ui.adapter.OnItemVideoClickedListener;
import com.hoanganhtuan01101995.sdk.utils.Utils;
import com.hoanganhtuan01101995.loadmore.view.LoadingFooter;
import com.hoanganhtuan01101995.loadmore.view.RecyclerViewStateUtils;

import butterknife.OnClick;

/**
 * Created by Hoang Anh Tuan on 11/10/2017.
 */

public class ListVideoPlaylistActivity extends Activity implements OnItemVideoClickedListener,
        ApiCallback<Video>,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener{

    private static final String PLAYLIST_ID = "playlistId";

    View vBgList;
    View vClose;
    ImageView ivBack;
    TextView tvTitle;
    ImageView ivClose;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout llListLoadmore;

    public static void start(Activity context, String playlistId) {
        Intent starter = new Intent(context, ListVideoPlaylistActivity.class);
        starter.putExtra(PLAYLIST_ID, playlistId);
        context.startActivity(starter);
        context.overridePendingTransition(0, 0);
    }

    private NormalAdapter normalAdapter;
    private String pageToken;
    private String playlistId;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
    }

    private void initView() {
        vBgList = findViewById(R.id.vBgList);
        vClose = findViewById(R.id.vClose);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        ivClose = findViewById(R.id.ivClose);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        llListLoadmore = findViewById(R.id.llListLoadmore);

        ivBack.setOnClickListener(this);

        tvTitle.setText(getString(R.string.listVideoPlaylist));
        pageToken = "";
        playlistId = getIntent().getStringExtra(PLAYLIST_ID);

        ivClose.setVisibility(View.GONE);
        ivBack.setVisibility(View.VISIBLE);
        llListLoadmore.setVisibility(View.VISIBLE);

        normalAdapter = new NormalAdapter(this);
        normalAdapter.setOnItemVideoClickedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(normalAdapter));
        recyclerView.addOnScrollListener(onScrollListener);


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }


    private EndlessRecyclerOnScrollListener onScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
            if (state == LoadingFooter.State.Loading) return;
            if (Utils.isNetworkConnected(ListVideoPlaylistActivity.this)) {
                if (count == 50) {
                    RecyclerViewStateUtils.setFooterViewState(ListVideoPlaylistActivity.this, recyclerView, 50, LoadingFooter.State.Loading, null);
                    getData();
                }
            } else
                RecyclerViewStateUtils.setFooterViewState(ListVideoPlaylistActivity.this, recyclerView, 50, LoadingFooter.State.NetWorkError, null);

        }
    };

    private void getData() {
        Api.callApiGetVideosByPlaylistId(pageToken, playlistId, this);
    }

    @Override
    public void onItemVideoClicked(Video video) {
        VideoActivity.start(this, video, VideoType.PLAYLIST, playlistId);
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
        swipeRefreshLayout.setRefreshing(false);
        RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void onRefresh() {
        pageToken = "";
        normalAdapter.clear();
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(normalAdapter));

        getData();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        finish();
        overridePendingTransition(0, 0);
    }
}
