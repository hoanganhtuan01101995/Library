package com.hoanganhtuan01101995.sdk.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoanganhtuan01101995.sdk.R;
import com.hoanganhtuan01101995.sdk.db.Playlist;
import com.hoanganhtuan01101995.sdk.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by Hoang Anh Tuan on 10/30/2017.
 */

public class PlaylistAdapter extends BaseAdapter<Playlist> {

    private OnItemPlaylistClickedListener onItemPlaylistClickedListener;

    public PlaylistAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_normal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final Playlist playlist = list.get(position);

        Picasso.with(activity)
                .load(playlist.getMedium())
                .resize(200, 200)
                .centerCrop()
                .into(viewHolder.img);

        viewHolder.tvTitle.setText(playlist.getTitle());
        viewHolder.tvDescription.setText(playlist.getDescription());
        viewHolder.tvTime.setText(Utils.getTimeAgo(playlist.getPublishedAt(), activity));

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemPlaylistClickedListener != null)
                    onItemPlaylistClickedListener.onPlaylistClicked(list.get(position));
            }
        });

    }

    public void setOnItemPlaylistClickedListener(OnItemPlaylistClickedListener onItemPlaylistClickedListener) {
        this.onItemPlaylistClickedListener = onItemPlaylistClickedListener;
    }

    public interface OnItemPlaylistClickedListener {
        void onPlaylistClicked(Playlist playlist);
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
