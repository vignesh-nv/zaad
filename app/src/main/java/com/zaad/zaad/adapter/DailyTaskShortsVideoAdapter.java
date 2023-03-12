package com.zaad.zaad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.zaad.zaad.R;
import com.zaad.zaad.listeners.ShortsPlayCompletedListener;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.Video;

import java.util.List;

public class DailyTaskShortsVideoAdapter extends RecyclerView.Adapter<DailyTaskShortsVideoAdapter.YoutubeShortsViewHolder> {

    List<DailyTaskVideo> videoList;
    ShortsPlayCompletedListener completedListener;

    public DailyTaskShortsVideoAdapter(final List<DailyTaskVideo> videoList, final ShortsPlayCompletedListener completedListener) {
        this.videoList = videoList;
        this.completedListener = completedListener;

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

        public void setVideoData(DailyTaskVideo video) {

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

                @Override
                public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                    super.onStateChange(youTubePlayer, state);
                    if (state == PlayerConstants.PlayerState.ENDED) {
                        completedListener.onCompleted(video);
                    }
                }
            });
        }
    }
}
