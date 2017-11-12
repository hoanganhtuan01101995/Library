package com.hoanganhtuan01101995.sdk.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoanganhtuan01101995.sdk.R;
import com.hoanganhtuan01101995.sdk.api.Api;
import com.hoanganhtuan01101995.sdk.api.ApiCallback;
import com.hoanganhtuan01101995.sdk.db.Video;
import com.hoanganhtuan01101995.sdk.db.VideoType;
import com.hoanganhtuan01101995.sdk.ui.adapter.NormalAdapter;
import com.hoanganhtuan01101995.sdk.ui.adapter.OnItemVideoClickedListener;

/**
 * Created by Hoang Anh Tuan on 10/23/2017.
 */

public class SearchActivity extends Activity implements TextWatcher,
        ApiCallback<Video>,
        OnItemVideoClickedListener,
        View.OnClickListener {


    public static void start(Activity context) {
        Intent starter = new Intent(context, SearchActivity.class);
        context.startActivity(starter);
        context.overridePendingTransition(0, 0);
    }

    RecyclerView listSearch;
    SwipeRefreshLayout refresh;
    EditText etSearch;
    TextView tvExit;
    RelativeLayout header;

    private NormalAdapter normalAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        listSearch = findViewById(R.id.listSearch);
        refresh = findViewById(R.id.refresh);
        etSearch = findViewById(R.id.etSearch);
        tvExit = findViewById(R.id.tvExit);
        header = findViewById(R.id.header);

        tvExit.setOnClickListener(this);
        refresh.setEnabled(false);

        etSearch.setFocusableInTouchMode(true);
        etSearch.requestFocus();
        etSearch.addTextChangedListener(this);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);

        normalAdapter = new NormalAdapter(this);
        normalAdapter.setOnItemVideoClickedListener(this);

        listSearch.setAdapter(normalAdapter);
        listSearch.setLayoutManager(new LinearLayoutManager(this));

        Api.callApiSearchVideosByName("a", this);
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
        overridePendingTransition(0, 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Api.callApiSearchVideosByName(etSearch.getText().toString(), this);
        normalAdapter.clear();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onNextPageToken(String nextPageToken) {

    }

    @Override
    public void onProgressUpdate(Video video) {
        if (video.getVideoId().isEmpty()) return;
        normalAdapter.add(video);
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onItemVideoClicked(Video video) {
        VideoActivity.start(this, video, VideoType.HOME, "");
    }

    @Override
    public void onClick(View view) {
        finish();
        overridePendingTransition(0, 0);
    }
}
