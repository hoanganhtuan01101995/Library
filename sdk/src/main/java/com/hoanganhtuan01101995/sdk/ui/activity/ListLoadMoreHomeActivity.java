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
import com.hoanganhtuan01101995.sdk.db.Video;
import com.hoanganhtuan01101995.sdk.db.VideoType;
import com.hoanganhtuan01101995.sdk.ui.adapter.NormalAdapter;
import com.hoanganhtuan01101995.sdk.ui.adapter.OnItemVideoClickedListener;
import com.hoanganhtuan01101995.sdk.utils.Utils;
import com.hoanganhtuan01101995.loadmore.view.LoadingFooter;
import com.hoanganhtuan01101995.loadmore.view.RecyclerViewStateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hoang Anh Tuan on 11/10/2017.
 */

public class ListLoadMoreHomeActivity extends Activity implements OnItemVideoClickedListener,
        ApiCallback<Video>, SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private static final String PAGE_TOKEN = "pageToken";

    View vBgList;
    View vClose;
    ImageView ivBack;
    TextView tvTitle;
    ImageView ivClose;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout llListLoadmore;

    public static void start(Activity context, String pageToken) {
        Intent starter = new Intent(context, ListLoadMoreHomeActivity.class);
        starter.putExtra(PAGE_TOKEN, pageToken);
        context.startActivity(starter);
        context.overridePendingTransition(0, 0);
    }

    private NormalAdapter normalAdapter;
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

        pageToken = getIntent().getStringExtra(PAGE_TOKEN);

        vBgList.setAlpha(0f);
        llListLoadmore.setTranslationY(getResources().getDimension(R.dimen.listLoadMoreHeight));

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
            if (Utils.isNetworkConnected(ListLoadMoreHomeActivity.this)) {
                if (count == 50) {
                    RecyclerViewStateUtils.setFooterViewState(ListLoadMoreHomeActivity.this, recyclerView, 50, LoadingFooter.State.Loading, null);
                    getData();
                }
            } else
                RecyclerViewStateUtils.setFooterViewState(ListLoadMoreHomeActivity.this, recyclerView, 50, LoadingFooter.State.NetWorkError, null);

        }
    };

    private void getData() {
        Api.callApiGetVideosNew(pageToken, this);
    }

    @Override
    public void onItemVideoClicked(Video video) {
        VideoActivity.start(this,video, VideoType.HOME,"");
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
        pageToken = getIntent().getStringExtra(PAGE_TOKEN);
        normalAdapter.clear();
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(normalAdapter));

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
