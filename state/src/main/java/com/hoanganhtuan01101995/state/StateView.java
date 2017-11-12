package com.hoanganhtuan01101995.state;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by HOANG ANH TUAN on 8/19/2017.
 */

public class StateView extends RelativeLayout {

    private OnRefreshClickedListener onRefreshClickedListener;

    private State state;

    private int titleNoData;
    private int descriptionNoData;
    private int titleNoInternet;
    private int descriptionNoInternet;

    private Button btnRefresh;
    private TextView tvTitle;
    private TextView tvDescription;

    public StateView(Context context) {
        super(context);
        init(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_state, this);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        btnRefresh = findViewById(R.id.btnRefresh);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateView);
            titleNoData = a.getResourceId(R.styleable.StateView_titleNoData, R.string.titleNotData);
            descriptionNoData = a.getResourceId(R.styleable.StateView_descriptionNoData, R.string.descriptionNotData);
            titleNoInternet = a.getResourceId(R.styleable.StateView_titleNoInternet, R.string.titleNotInternet);
            descriptionNoInternet = a.getResourceId(R.styleable.StateView_descriptionNoInternet, R.string.descriptionNotInternet);
            a.recycle();
        }
        btnRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRefreshClickedListener != null) onRefreshClickedListener.onRefreshClicked();
            }
        });
        changeState(State.NORMAL);
    }

    public void setOnRefreshClickedListener(OnRefreshClickedListener onRefreshClickedListener) {
        this.onRefreshClickedListener = onRefreshClickedListener;
    }

    public void setTitleNoData(int titleNoData) {
        this.titleNoData = titleNoData;
    }

    public void setDescriptionNoData(int descriptionNoData) {
        this.descriptionNoData = descriptionNoData;
    }

    public void setTitleNoInternet(int titleNoInternet) {
        this.titleNoInternet = titleNoInternet;
    }

    public void setDescriptionNoInternet(int descriptionNoInternet) {
        this.descriptionNoInternet = descriptionNoInternet;
    }

    public void changeState(State state) {
        if (this.state != null && this.state == state) return;
        this.state = state;
        switch (this.state) {
            case NO_DATA:
                btnRefresh.setVisibility(GONE);
                tvTitle.setText(getContext().getString(titleNoData));
                tvDescription.setText(getContext().getString(descriptionNoData));
                this.setVisibility(VISIBLE);
                break;
            case NO_NETWORK:
                btnRefresh.setVisibility(VISIBLE);
                tvTitle.setText(getContext().getString(titleNoInternet));
                tvDescription.setText(getContext().getString(descriptionNoInternet));
                this.setVisibility(VISIBLE);
                break;
            case NORMAL:
                this.setVisibility(INVISIBLE);
                break;
        }
    }

    public State getState() {
        return state;
    }

    public interface OnRefreshClickedListener {
        void onRefreshClicked();
    }

    public enum State {
        NO_DATA, NO_NETWORK, NORMAL
    }
}
