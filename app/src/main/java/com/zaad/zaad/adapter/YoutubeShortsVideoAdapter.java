package com.zaad.zaad.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    int currentVisibleItem;

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
        holder.setVideoData(videoList.get(position), position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof LinearLayoutManager && getItemCount() > 0) {
            LinearLayoutManager llm = (LinearLayoutManager) manager;
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                    if(visiblePosition > -1) {
                        Log.i("DailyTaskShortsVideo", "Visible" + String.valueOf(visiblePosition));
                        currentVisibleItem = visiblePosition;
                    }
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull YoutubeShortsViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.player != null) {
            holder.player.play();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull YoutubeShortsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.player != null) {
            holder.player.pause();
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class YoutubeShortsViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView;
        ProgressBar progressBar;

        YouTubePlayer player;

        public YoutubeShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
        }

        public void setVideoData(Video video, int currentVisiblePosition) {

            IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                    .controls(0)
                    .build();
            try {
                youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        super.onReady(youTubePlayer);
                        player = youTubePlayer;
                    }
                }, true, iFramePlayerOptions);

            } catch (IllegalStateException e) {

                if (currentVisiblePosition != currentVisibleItem) {
                    player.cueVideo(video.getVideoUrl(), 0);
                } else {
                    player.loadVideo(video.getVideoUrl(), 0);
                }
                return;
            }
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    if (currentVisiblePosition != currentVisibleItem) {
                        youTubePlayer.cueVideo(video.getVideoUrl(), 0);
                    } else {
                        youTubePlayer.loadVideo(video.getVideoUrl(), 0);
                    }
                }

                @Override
                public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                    super.onStateChange(youTubePlayer, state);
                    if (state == PlayerConstants.PlayerState.ENDED) {
                    }
                }
            });
        }
    }
}
