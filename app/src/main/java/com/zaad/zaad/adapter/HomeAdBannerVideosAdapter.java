package com.zaad.zaad.adapter;

import static android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT;
import static android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.ChildVideoPlayerActivity;
import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.Video;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class HomeAdBannerVideosAdapter extends RecyclerView.Adapter<HomeAdBannerVideosAdapter.VideoViewHolder> {

    private List<AdBanner> itemList;
    private Context context;

    public HomeAdBannerVideosAdapter(List<AdBanner> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.home_ad_banner,
                        viewGroup, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder viewHolder, int position) {

        AdBanner adBanner = itemList.get(position);
        ImageLoader imageLoader = Coil.imageLoader(context);

        ImageRequest request = new ImageRequest.Builder(context)
                .data(adBanner.getImageUrl())
                .crossfade(true)
                .target(viewHolder.imageView)
                .build();
        imageLoader.enqueue(request);

        viewHolder.videoView.setVideoPath(adBanner.getVideoUrl());
        viewHolder.videoView.requestFocus();
        viewHolder.videoView.start();

        viewHolder.videoView.setOnPreparedListener(mediaPlayer -> {
            viewHolder.videoView.seekTo(0);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(0, 0);
            mediaPlayer.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mediaPlayer.start();
            viewHolder.imageView.setVisibility(View.GONE);

            float videoRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
            float screenRatio = viewHolder.videoView.getWidth() / (float) viewHolder.videoView.getHeight();
            float scaleX = videoRatio / screenRatio;
            if (scaleX >= 1f) {
                viewHolder.videoView.setScaleX(scaleX);
            } else {
                viewHolder.videoView.setScaleY(1f / scaleX);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private VideoView videoView;

        VideoViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ad_image);
            videoView = itemView.findViewById(R.id.ad_video);
        }
    }
}