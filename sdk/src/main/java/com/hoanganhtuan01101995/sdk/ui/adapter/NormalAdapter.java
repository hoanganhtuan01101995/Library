package com.hoanganhtuan01101995.sdk.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoanganhtuan01101995.sdk.R;
import com.hoanganhtuan01101995.sdk.Sdk;
import com.hoanganhtuan01101995.sdk.db.Video;
import com.hoanganhtuan01101995.sdk.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by Hoang Anh Tuan on 11/10/2017.
 */

public class NormalAdapter extends BaseAdapter<Video> {
    private OnItemVideoClickedListener onItemVideoClickedListener;

    public NormalAdapter(Activity activity) {
        super(activity);
    }

    public void setOnItemVideoClickedListener(OnItemVideoClickedListener onItemVideoClickedListener) {
        this.onItemVideoClickedListener = onItemVideoClickedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_normal, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final Video video = list.get(position);

        viewHolder.tvTitle.setText(video.getTitle());
        viewHolder.tvDescription.setText(video.getDescription());
        viewHolder.tvTime.setText(Utils.getTimeAgo(video.getPublishedAt(), activity));

        Picasso.with(activity)
                .load("https://img.youtube.com/vi/" + video.getVideoId() + "/"+ Sdk.IMG_TYPE+".jpg")
                .resize(200, 200)
                .centerCrop()
                .into(viewHolder.img);

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
        ImageView img;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvTime;
        LinearLayout item;

        ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvTime = view.findViewById(R.id.tvTime);
            item = view.findViewById(R.id.item);
        }
    }
}
