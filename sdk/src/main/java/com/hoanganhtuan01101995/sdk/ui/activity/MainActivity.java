package com.hoanganhtuan01101995.sdk.ui.activity;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hoanganhtuan01101995.admob.Admob;
import com.hoanganhtuan01101995.admob.interstitial.InterstitialAdServicer;
import com.hoanganhtuan01101995.admob.video.VideoAdService;
import com.hoanganhtuan01101995.sdk.R;
import com.hoanganhtuan01101995.sdk.Sdk;
import com.hoanganhtuan01101995.sdk.api.Api;
import com.hoanganhtuan01101995.sdk.api.ApiCallback;
import com.hoanganhtuan01101995.sdk.db.Video;
import com.hoanganhtuan01101995.sdk.db.VideoType;
import com.hoanganhtuan01101995.sdk.ui.adapter.OnItemVideoClickedListener;
import com.hoanganhtuan01101995.sdk.ui.adapter.VideoAdapter;
import com.hoanganhtuan01101995.sdk.utils.Utils;
import com.hoanganhtuan01101995.update.Update;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener,
        ApiCallback<Video>,
        OnItemVideoClickedListener,
        View.OnClickListener {

    ImageView ivSearch;
    ImageView ivAdvertisement;
    RelativeLayout header;
    ImageView ivShare;
    TextView tvShare;
    LinearLayout llShare;
    ImageView ivLoadmore;
    TextView tvLoadmore;
    LinearLayout llLoadmore;
    ImageView ivRate;
    TextView tvRate;
    LinearLayout llRate;
    ImageView ivCategory;
    TextView tvCategory;
    LinearLayout llCategory;
    LinearLayout footer;
    TextView tvSave;
    TextView tvHome;
    TextView tvHistory;
    DiscreteScrollView videos;
    SwipeRefreshLayout refresh;
    ImageView ivStar;
    TextSwitcher tsTitle;
    TextSwitcher tsDescription;
    ImageView ivClock;
    TextSwitcher tsTime;

    private VideoAdapter videoAdapter;
    private InfiniteScrollAdapter infiniteAdapter;

    private int currentPosition;

    private String pageToken;
    private Type type;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        configApp();
    }

    private void initView() {
        ivSearch = findViewById(R.id.ivSearch);
        ivAdvertisement = findViewById(R.id.ivAdvertisement);
        header = findViewById(R.id.header);
        ivShare = findViewById(R.id.ivShare);
        tvShare = findViewById(R.id.tvShare);
        llShare = findViewById(R.id.llShare);
        ivLoadmore = findViewById(R.id.ivLoadmore);
        tvLoadmore = findViewById(R.id.tvLoadmore);
        llLoadmore = findViewById(R.id.llLoadmore);
        ivRate = findViewById(R.id.ivRate);
        tvRate = findViewById(R.id.tvRate);
        llRate = findViewById(R.id.llRate);
        ivCategory = findViewById(R.id.ivCategory);
        tvCategory = findViewById(R.id.tvCategory);
        llCategory = findViewById(R.id.llCategory);
        footer = findViewById(R.id.footer);
        tvSave = findViewById(R.id.tvSave);
        tvHome = findViewById(R.id.tvHome);
        tvHistory = findViewById(R.id.tvHistory);
        videos = findViewById(R.id.videos);
        refresh = findViewById(R.id.refresh);
        ivStar = findViewById(R.id.ivStar);
        tsTitle = findViewById(R.id.tsTitle);
        tsDescription = findViewById(R.id.tsDescription);
        ivClock = findViewById(R.id.ivClock);
        tsTime = findViewById(R.id.tsTime);

        ivSearch.setOnClickListener(this);
        ivAdvertisement.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llLoadmore.setOnClickListener(this);
        llRate.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        tvHistory.setOnClickListener(this);

        type = Type.HOME;
        pageToken = "";

        tsTitle.setFactory(new TitleFactory());

        tsTime.setFactory(new TimeFactory());

        tsDescription.setInAnimation(this, android.R.anim.fade_in);
        tsDescription.setOutAnimation(this, android.R.anim.fade_out);
        tsDescription.setFactory(new DescriptionFactory());

        videoAdapter = new VideoAdapter(this);
        videoAdapter.setOnItemVideoClickedListener(this);

        infiniteAdapter = InfiniteScrollAdapter.wrap(videoAdapter);
        videos.setOrientation(Orientation.HORIZONTAL);
        videos.setAdapter(infiniteAdapter);
        videos.setHasFixedSize(true);
        videos.addOnItemChangedListener(this);
        videos.setItemTransitionTimeMillis(120);
        videos.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        refresh.setEnabled(false);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });
    }

    private void getData() {
        count = 0;
        pageToken = "";

        videoAdapter.clear();

        ivStar.setVisibility(View.GONE);
        ivClock.setVisibility(View.GONE);

        refresh.setRefreshing(true);

        tsTitle.setText("");
        tsDescription.setText("");
        tsTime.setText("");

        tvSave.setTextColor(ContextCompat.getColor(this, R.color.colorNormal));
        tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorNormal));
        tvHistory.setTextColor(ContextCompat.getColor(this, R.color.colorNormal));
        switch (type) {
            case HOME:
                tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                Api.callApiGetVideosNew(pageToken, this);
                break;
            case SAVE:
                tvSave.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                Api.callApiGetVideosFromSave(this);
                break;
            case HISTORY:
                tvHistory.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                Api.callApiGetVideoFromHistory(this);
                break;
        }
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        onActiveCardChange(positionInDataSet);
    }

    private void onActiveCardChange(int pos) {

        if (pos >= videoAdapter.getItemCount() || pos < 0) return;

        ivStar.setVisibility(View.VISIBLE);
        ivClock.setVisibility(View.VISIBLE);

        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        tsTitle.setInAnimation(this, animV[0]);
        tsTitle.setOutAnimation(this, animV[1]);
        tsTitle.setText(videoAdapter.getData().get(pos).getTitle());

        tsDescription.setText(videoAdapter.getData().get(pos).getDescription());

        tsTime.setInAnimation(this, animH[0]);
        tsTime.setInAnimation(this, animH[1]);
        tsTime.setText(Utils.getTimeAgo(videoAdapter.getData().get(pos).getPublishedAt(), this));

        currentPosition = pos;

        if (currentPosition + 1 == videoAdapter.getData().size() && count == 50)
            llLoadmore.setVisibility(View.VISIBLE);
        if (currentPosition + 1 != videoAdapter.getData().size())
            llLoadmore.setVisibility(View.GONE);
    }

    @Override
    public void onNextPageToken(String nextPageToken) {
        pageToken = nextPageToken;
    }

    @Override
    public void onProgressUpdate(Video video) {
        count++;
        if (video.getVideoId().isEmpty()) return;
        videoAdapter.add(video);
    }

    @Override
    public void onComplete() {
        refresh.setRefreshing(false);
    }

    @Override
    public void onItemVideoClicked(Video video) {
        switch (type) {
            case HOME:
                VideoActivity.start(this, video, VideoType.HOME, "");
                break;
            case SAVE:
                VideoActivity.start(this, video, VideoType.SAVE, "");
                break;
            case HISTORY:
                VideoActivity.start(this, video, VideoType.HISTORY, "");
                break;
        }
    }

    private InterstitialAdServicer interstitialAdServicer;
    private VideoAdService videoAdService;

    private void configApp() {
        Update.with(this)
                .build();
        interstitialAdServicer = Admob.with(this)
                .asInterstitialAd()
                .setInterstitialAdId(Sdk.INTERSTITIAL_KEY)
                .builder();

        videoAdService = Admob.with(this)
                .asVideo()
                .setVideoAdId(Sdk.REWARDED_VIDEO_KEY)
                .builder();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        interstitialAdServicer.show();
    }

    @Override
    public void onResume() {
        videoAdService.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        videoAdService.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        videoAdService.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivSearch) {
            SearchActivity.start(this);
        } else if (view.getId() == R.id.ivAdvertisement) {
            videoAdService.show();
        } else if (view.getId() == R.id.llShare) {
            Utils.share(this, "https://play.google.com/store/apps/details?id=" + getPackageName());
        } else if (view.getId() == R.id.llLoadmore) {
            ListLoadMoreHomeActivity.start(this, pageToken);
        } else if (view.getId() == R.id.llRate) {
            Utils.rateApp(this);
        } else if (view.getId() == R.id.llCategory) {
            ListPlaylistActivity.start(this);
        } else if (view.getId() == R.id.tvSave) {
            type = Type.SAVE;
            getData();
        } else if (view.getId() == R.id.tvHome) {
            type = Type.HOME;
            getData();
        } else if (view.getId() == R.id.tvHistory) {
            type = Type.HISTORY;
            getData();
        }
    }

    public enum Type implements Serializable {
        HOME, SAVE, HISTORY
    }

    private class TitleFactory implements ViewSwitcher.ViewFactory {

        TitleFactory() {
        }

        @Override
        public View makeView() {
            Typeface mFont = Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf");

            TextView textView = new TextView(MainActivity.this);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(MainActivity.this, R.style.TitleTextView);
            } else {
                textView.setTextAppearance(R.style.TitleTextView);
            }
            textView.setMaxLines(2);
            textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTypeface(mFont);

            return textView;
        }

    }

    private class DescriptionFactory implements ViewSwitcher.ViewFactory {

        DescriptionFactory() {
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            TextView textView = new TextView(MainActivity.this);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(MainActivity.this, R.style.DescriptionTextView);
            } else {
                textView.setTextAppearance(R.style.DescriptionTextView);
            }
            textView.setMaxLines(3);
            return textView;
        }

    }

    private class TimeFactory implements ViewSwitcher.ViewFactory {

        TimeFactory() {
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            TextView textView = new TextView(MainActivity.this);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(MainActivity.this, R.style.TimeTextView);
            } else {
                textView.setTextAppearance(R.style.TimeTextView);
            }
            textView.setMaxLines(1);
            textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlue));
            return textView;
        }

    }
}
