package com.zaad.zaad.adapter;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.zaad.zaad.R;
import com.zaad.zaad.model.Video;

import java.util.List;

public class YoutubeShortsVideoAdapter extends RecyclerView.Adapter<YoutubeShortsVideoAdapter.YoutubeShortsViewHolder> {

    List<Video> videoList;

    public YoutubeShortsVideoAdapter(final List<Video> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public YoutubeShortsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YoutubeShortsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.youtube_shorts_item,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeShortsViewHolder holder, int position) {
        holder.setVideoData(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class YoutubeShortsViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView;
        ProgressBar progressBar;

        public YoutubeShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
        }

        public void setVideoData(Video video) {

            IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                    .controls(0)
                    .build();
            youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                }
            }, true, iFramePlayerOptions);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(video.getVideoUrl(), 0);
                }
            });
        }
    }
}
