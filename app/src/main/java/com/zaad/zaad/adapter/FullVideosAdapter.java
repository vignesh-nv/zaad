package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.YoutubeVideoPlayerActivity;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.Video;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class FullVideosAdapter extends RecyclerView.Adapter<FullVideosAdapter.VideoViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Video> itemList;
    private Context context;

    public FullVideosAdapter(List<Video> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.video_item,
                        viewGroup, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder viewHolder, int position) {

        Video parentItem = itemList.get(position);
        ImageLoader imageLoader = Coil.imageLoader(context);

        viewHolder.imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, YoutubeVideoPlayerActivity.class);
            intent.putExtra("VIDEO_ID", parentItem.getVideoUrl());
            intent.putExtra("CATEGORY", parentItem.getCategory());
            context.startActivity(intent);
        });

        ImageRequest request = new ImageRequest.Builder(context)
                .data(parentItem.getImageUrl())
                .crossfade(true)
                .target(viewHolder.imageView)
                .build();
        imageLoader.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        VideoViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.video_thumbnail);
        }
    }
}