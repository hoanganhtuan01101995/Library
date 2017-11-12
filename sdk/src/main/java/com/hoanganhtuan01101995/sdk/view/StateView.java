package com.hoanganhtuan01101995.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoanganhtuan01101995.sdk.R;

/**
 * Created by HOANG ANH TUAN on 8/19/2017.
 */

public class StateView extends RelativeLayout {

    private State state;

    private int drawableNoData;
    private int titleNoData;
    private int descriptionNoData;
    private int drawableNoInternet;
    private int titleNoInternet;
    private int descriptionNoInternet;

    private ImageView imgTitle;
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

        imgTitle = this.findViewById(R.id.img_title);
        tvTitle = this.findViewById(R.id.tv_title);
        tvDescription = this.findViewById(R.id.tv_description);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateView);
            drawableNoData = a.getResourceId(R.styleable.StateView_drawableNoData, R.drawable.ic_not_data);
            titleNoData = a.getResourceId(R.styleable.StateView_titleNoData, R.string.app_name);
            descriptionNoData = a.getResourceId(R.styleable.StateView_descriptionNoData, R.string.app_name);
            drawableNoInternet = a.getResourceId(R.styleable.StateView_drawableNoInternet, R.drawable.ic_not_internet);
            titleNoInternet = a.getResourceId(R.styleable.StateView_titleNoInternet, R.string.app_name);
            descriptionNoInternet = a.getResourceId(R.styleable.StateView_descriptionNoInternet, R.string.app_name);
            a.recycle();
        }
        changeState(State.NORMAL);
    }

    public void setDrawableNoData(int drawableNoData) {
        this.drawableNoData = drawableNoData;
    }

    public void setTitleNoData(int titleNoData) {
        this.titleNoData = titleNoData;
    }

    public void setDescriptionNoData(int descriptionNoData) {
        this.descriptionNoData = descriptionNoData;
    }

    public void setDrawableNoInternet(int drawableNoInternet) {
        this.drawableNoInternet = drawableNoInternet;
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
                imgTitle.setImageResource(drawableNoData);
                tvTitle.setText(getContext().getString(titleNoData));
                tvDescription.setText(getContext().getString(descriptionNoData));
                this.setVisibility(VISIBLE);
                break;
            case NO_NETWORK:
                imgTitle.setImageResource(drawableNoInternet);
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

    public enum State {
        NO_DATA, NO_NETWORK, NORMAL
    }
}
