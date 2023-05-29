package com.zaad.zaad.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.AdDetailActivity;
import com.zaad.zaad.activity.ShortsActivity;
import com.zaad.zaad.VideoType;
import com.zaad.zaad.activity.VideoPlayerActivity;
import com.zaad.zaad.activity.YoutubeShortsActivity;
import com.zaad.zaad.activity.YoutubeVideoPlayerActivity;
import com.zaad.zaad.model.Video;

import java.io.Serializable;
import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class HomeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Video> videoList;
    private Context context;

    private String category;

    private String collection;

    HomeItemAdapter(List<Video> videoList, Context context, String category) {
        this.videoList = videoList;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (category.equals(VideoType.YOUTUBE_VIDEO.name())) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.home_child_item,
                            viewGroup, false);

            return new VideoViewHolder(view);
        } else if (category.equals(VideoType.FACEBOOK_VIDEOS.name())) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.home_child_item,
                            viewGroup, false);

            return new VideoViewHolder(view);
        } else if (category.equals("OFFLINE_STORE") || category.equals("ONLINE_STORE")) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.home_store_item,
                            viewGroup, false);

            return new StoreViewHolder(view);
        } else if (category.equals(VideoType.IMAGE_AD.name())) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.home_ad_item,
                            viewGroup, false);

            return new VideoViewHolder(view);
        } else {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.shorts_item,
                            viewGroup, false);

            return new ShortsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (category.equals(VideoType.YOUTUBE_VIDEO.name())) {
            VideoViewHolder childViewHolder = (VideoViewHolder) viewHolder;
            Video childItem = videoList.get(position);

            ImageLoader imageLoader = Coil.imageLoader(context);

            childViewHolder.imageView.setOnClickListener(view -> {
                Intent intent = new Intent(context, YoutubeVideoPlayerActivity.class);
                intent.putExtra("VIDEO_ID", childItem.getVideoUrl());
                intent.putExtra("CATEGORY", childItem.getCategory());
                intent.putExtra("TITLE", childItem.getTitle());
                context.startActivity(intent);
            });
            ImageRequest request = new ImageRequest.Builder(context)
                    .data(childItem.getImageUrl())
                    .crossfade(true)
                    .target(childViewHolder.imageView)
                    .build();
            imageLoader.enqueue(request);
        } else if (category.equals(VideoType.FACEBOOK_VIDEOS.name())) {
            VideoViewHolder childViewHolder = (VideoViewHolder) viewHolder;
            Video childItem = videoList.get(position);
            ImageLoader imageLoader = Coil.imageLoader(context);

            childViewHolder.imageView.setOnClickListener(view -> {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("VIDEO_URL", childItem.getVideoUrl());
                context.startActivity(intent);
            });

            ImageRequest request = new ImageRequest.Builder(context)
                    .data(childItem.getImageUrl())
                    .crossfade(true)
                    .target(childViewHolder.imageView)
                    .build();
            imageLoader.enqueue(request);
        } else if (category.equals(VideoType.YOUTUBE_SHORTS.name())) {
            ShortsViewHolder shortsViewHolder = (ShortsViewHolder) viewHolder;
            Video childItem = videoList.get(position);

            ImageLoader imageLoader = Coil.imageLoader(context);

            shortsViewHolder.imageView.setOnClickListener(view -> {
                Intent intent = new Intent(context, YoutubeShortsActivity.class);
                intent.putExtra("VIDEO_URL", childItem.getVideoUrl());
                context.startActivity(intent);
            });

            ImageRequest request = new ImageRequest.Builder(context)
                    .data(childItem.getImageUrl())
                    .crossfade(true)
                    .target(shortsViewHolder.imageView)
                    .build();
            imageLoader.enqueue(request);
        } else if (category.equals("OFFLINE_STORE") || category.equals("ONLINE_STORE")) {
            Video childItem = videoList.get(position);
            ImageLoader imageLoader = Coil.imageLoader(context);
            StoreViewHolder storeViewHolder = (StoreViewHolder) viewHolder;
            ImageRequest request = new ImageRequest.Builder(context)
                    .data(childItem.getImageUrl())
                    .crossfade(true)
                    .target(storeViewHolder.imageView)
                    .build();
            imageLoader.enqueue(request);

        } else if (category.equals(VideoType.IMAGE_AD.name())) {

            VideoViewHolder childViewHolder = (VideoViewHolder) viewHolder;
            Video childItem = videoList.get(position);

            ImageLoader imageLoader = Coil.imageLoader(context);

            childViewHolder.imageView.setOnClickListener(view -> {
                Intent intent = new Intent(context, AdDetailActivity.class);
                intent.putExtra("IMAGE_URL", childItem.getImageUrl());
                intent.putExtra("AD_ID", childItem.getId());
                context.startActivity(intent);
            });

            ImageRequest request = new ImageRequest.Builder(context)
                    .data(childItem.getImageUrl())
                    .crossfade(true)
                    .target(childViewHolder.imageView)
                    .build();
            imageLoader.enqueue(request);

        } else {
            ShortsViewHolder shortsViewHolder = (ShortsViewHolder) viewHolder;
            Video childItem = videoList.get(position);

            ImageLoader imageLoader = Coil.imageLoader(context);

            shortsViewHolder.imageView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ShortsActivity.class);
                intent.putExtra("VIDEO", childItem);
                intent.putExtra("COLLECTION", collection);
                context.startActivity(intent);
            });

            ImageRequest request = new ImageRequest.Builder(context)
                    .data(childItem.getImageUrl())
                    .crossfade(true)
                    .target(shortsViewHolder.imageView)
                    .build();
            imageLoader.enqueue(request);
        }
    }

    public void setCollection(String collection) {
        this.collection = collection;
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

    class StoreViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        StoreViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.store_image);
        }
    }

    class ShortsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ShortsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_child_item);
        }
    }
}

