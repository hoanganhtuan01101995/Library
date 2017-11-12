package com.hoanganhtuan01101995.sdk.ui.activity;

import android.animation.Animator;
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
import com.hoanganhtuan01101995.sdk.db.Playlist;
import com.hoanganhtuan01101995.sdk.ui.adapter.PlaylistAdapter;
import com.hoanganhtuan01101995.sdk.utils.Utils;
import com.hoanganhtuan01101995.loadmore.view.LoadingFooter;
import com.hoanganhtuan01101995.loadmore.view.RecyclerViewStateUtils;

/**
 * Created by Hoang Anh Tuan on 11/10/2017.
 */

public class ListPlaylistActivity extends Activity implements PlaylistAdapter.OnItemPlaylistClickedListener,
        ApiCallback<Playlist>,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {

    View vBgList;
    View vClose;
    ImageView ivBack;
    TextView tvTitle;
    ImageView ivClose;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout llListLoadmore;

    public static void start(Activity context) {
        Intent starter = new Intent(context, ListPlaylistActivity.class);
        context.startActivity(starter);
        context.overridePendingTransition(0, 0);
    }

    private PlaylistAdapter playlistAdapter;
    private String pageToken;
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

        vClose.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        tvTitle.setText(getString(R.string.category));
        pageToken = "";

        vBgList.setAlpha(0f);
        llListLoadmore.setTranslationY(getResources().getDimension(R.dimen.listLoadMoreHeight));

        vBgList.post(new Runnable() {
            @Override
            public void run() {
                vBgList.setVisibility(View.VISIBLE);
                llListLoadmore.setVisibility(View.VISIBLE);
                vBgList.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .start();
                llListLoadmore.animate()
                        .translationY(0)
                        .setDuration(300)
                        .start();

            }
        });

        playlistAdapter = new PlaylistAdapter(this);
        playlistAdapter.setOnItemPlaylistClickedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(playlistAdapter));
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
            if (Utils.isNetworkConnected(ListPlaylistActivity.this)) {
                if (count == 50) {
                    RecyclerViewStateUtils.setFooterViewState(ListPlaylistActivity.this, recyclerView, 50, LoadingFooter.State.Loading, null);
                    getData();
                }
            } else
                RecyclerViewStateUtils.setFooterViewState(ListPlaylistActivity.this, recyclerView, 50, LoadingFooter.State.NetWorkError, null);

        }
    };

    private void getData() {
        Api.callApiGetPlaylists(pageToken, this);
    }

    @Override
    public void onPlaylistClicked(Playlist playlist) {
        ListVideoPlaylistActivity.start(this, playlist.getId());
    }

    @Override
    public void onNextPageToken(String nextPageToken) {
        pageToken = nextPageToken;
    }

    @Override
    public void onProgressUpdate(Playlist playlist) {
        count++;
        if (playlist.getId().isEmpty()) return;
        playlistAdapter.add(playlist);
    }

    @Override
    public void onComplete() {
        swipeRefreshLayout.setRefreshing(false);
        RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void onRefresh() {
        pageToken = "";
        playlistAdapter.clear();
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(playlistAdapter));

        getData();
    }

    @Override
    public void onClick(View view) {
        close();
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void close() {

        vBgList.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        vBgList.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
        llListLoadmore.animate()
                .translationY(getResources().getDimension(R.dimen.listLoadMoreHeight))
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        finish();
                        overridePendingTransition(0, 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }
}

