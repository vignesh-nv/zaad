package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.YoutubeVideoPlayerActivity;
import com.zaad.zaad.model.Video;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ChildVideoItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Video> videoList;
    private Context context;

    private String category;

    ChildVideoItemAdapter(List<Video> videoList, Context context, String category) {
        this.videoList = videoList;
        this.context = context;
        this.category = category;
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
        Video childItem = videoList.get(position);

        ImageLoader imageLoader = Coil.imageLoader(context);

        childViewHolder.imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, YoutubeVideoPlayerActivity.class);
            intent.putExtra("VIDEO_ID", childItem.getVideoUrl());
            intent.putExtra("CATEGORY", childItem.getCategory());
            intent.putExtra("COLLECTION", "");
            context.startActivity(intent);
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

