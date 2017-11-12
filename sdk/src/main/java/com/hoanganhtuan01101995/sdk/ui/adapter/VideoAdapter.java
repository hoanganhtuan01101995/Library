package com.hoanganhtuan01101995.sdk.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hoanganhtuan01101995.sdk.R;
import com.hoanganhtuan01101995.sdk.db.Video;
import com.squareup.picasso.Picasso;

/**
 * Created by Hoang Anh Tuan on 11/2/2017.
 */

public class VideoAdapter extends BaseAdapter<Video> {

    private OnItemVideoClickedListener onItemVideoClickedListener;

    public VideoAdapter(Activity activity) {
        super(activity);
    }

    public void setOnItemVideoClickedListener(OnItemVideoClickedListener onItemVideoClickedListener) {
        this.onItemVideoClickedListener = onItemVideoClickedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_video, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final Video video = list.get(position);

        Picasso.with(activity)
                .load("https://img.youtube.com/vi/" + video.getVideoId() + "/maxresdefault.jpg")
                .resize(500, 500)
                .centerCrop()
                .into(viewHolder.iv);


        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemVideoClickedListener != null) {
                    onItemVideoClickedListener.onItemVideoClicked(video);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        RelativeLayout item;

        ViewHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.iv);
            item = view.findViewById(R.id.item);
        }
    }
}
