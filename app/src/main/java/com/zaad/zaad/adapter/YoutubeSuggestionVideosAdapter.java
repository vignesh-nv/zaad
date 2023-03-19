package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.YoutubeVideoPlayerActivity;
import com.zaad.zaad.listeners.OnSuggestionVideoClick;
import com.zaad.zaad.model.Video;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class YoutubeSuggestionVideosAdapter extends RecyclerView.Adapter<YoutubeSuggestionVideosAdapter.YoutubeSuggestionVideoViewHolder> {

    private List<Video> itemList;
    private Context context;
    private OnSuggestionVideoClick onSuggestionVideoClick;
    public YoutubeSuggestionVideosAdapter(List<Video> itemList, Context context, OnSuggestionVideoClick onSuggestionVideoClick) {
        this.itemList = itemList;
        this.context = context;
        this.onSuggestionVideoClick = onSuggestionVideoClick;
    }

    @NonNull
    @Override
    public YoutubeSuggestionVideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.youtube_suggestion_video_item,
                        viewGroup, false);

        return new YoutubeSuggestionVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeSuggestionVideoViewHolder viewHolder, int position) {

        Video parentItem = itemList.get(position);
        ImageLoader imageLoader = Coil.imageLoader(context);

//        viewHolder.imageView.setOnClickListener(view -> Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show());

        ImageRequest request = new ImageRequest.Builder(context)
                .data(parentItem.getImageUrl())
                .crossfade(true)
                .target(viewHolder.imageView)
                .build();

        imageLoader.enqueue(request);

        viewHolder.imageView.setOnClickListener(view -> {
            onSuggestionVideoClick.onClick(parentItem);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class YoutubeSuggestionVideoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        YoutubeSuggestionVideoViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.video_thumbnail);
        }
    }
}