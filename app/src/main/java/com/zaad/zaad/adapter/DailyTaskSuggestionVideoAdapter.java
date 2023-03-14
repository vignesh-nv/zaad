package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.DailyTaskVideoActivity;
import com.zaad.zaad.activity.YoutubeVideoPlayerActivity;
import com.zaad.zaad.listeners.DailyTaskSuggestionVideoClickListener;
import com.zaad.zaad.listeners.OnSuggestionVideoClick;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.Video;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class DailyTaskSuggestionVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DailyTaskVideo> videoList;
    private Context context;

    private DailyTaskSuggestionVideoClickListener suggestionVideoClickListener;

    public DailyTaskSuggestionVideoAdapter(List<DailyTaskVideo> videoList, Context context,
                                           DailyTaskSuggestionVideoClickListener onSuggestionVideoClick) {
        this.videoList = videoList;
        this.context = context;
        this.suggestionVideoClickListener = onSuggestionVideoClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.home_child_item,
                        viewGroup, false);

        return new VideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        VideoViewHolder childViewHolder = (VideoViewHolder) viewHolder;
        DailyTaskVideo childItem = videoList.get(position);

        ImageLoader imageLoader = Coil.imageLoader(context);

        childViewHolder.imageView.setOnClickListener(view -> {
            this.suggestionVideoClickListener.onClick(childItem);
        });

        ImageRequest request = new ImageRequest.Builder(context)
                .data(childItem.getImageUrl())
                .crossfade(true)
                .target(childViewHolder.imageView)
                .build();
        imageLoader.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        VideoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_child_item);
        }
    }

}

