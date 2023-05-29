package com.zaad.zaad.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyTaskShortsVideoAdapter extends RecyclerView.Adapter<DailyTaskShortsVideoAdapter.YoutubeShortsViewHolder> {

    List<DailyTaskVideo> videoList;
    ShortsPlayCompletedListener completedListener;

    int currentVisibleItem;

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
        Log.i("DailyShortsAdapter", position + ": " + videoList.get(position).getTaskId());
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
                        Log.i("DailyShortsAdapter", "Visible: " + (visiblePosition + 1));
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
            Log.i("DailyShortsAdapter", "inside onViewAttachedToWindow" + holder.player);
            holder.player.play();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull YoutubeShortsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.player != null) {
            Log.i("DailyShortsAdapter", "inside onViewDetachedFromWindow");
            holder.player.pause();
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class YoutubeShortsViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView;
        YouTubePlayer player;

        int currentCueVideo;

        public YoutubeShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
        }

        public void setVideoData(DailyTaskVideo video, int currentVisiblePosition) {

            IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                    .controls(0)
                    .build();

            try {
                youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        super.onReady(youTubePlayer);
                        player = youTubePlayer;
                        Log.i("DailyShortsAdapter", "Initialized player: " + (currentVisiblePosition + 1));
                    }
                }, true, iFramePlayerOptions);
            } catch (IllegalStateException e) {
                if (currentVisiblePosition != currentVisibleItem) {
                    Log.i("DailyShortsAdapter", "cuevideo" + (currentVisiblePosition + 1));
                    player.cueVideo(video.getVideoUrl(), 0);
                } else {
                    Log.i("DailyShortsAdapter", "loadvideo" + (currentVisiblePosition + 1));
                    player.loadVideo(video.getVideoUrl(), 0);
                    currentCueVideo = currentVisiblePosition;
                }

                Log.i("DailyShortsAdapter", "Error Occurred: " + e.getMessage() + (currentVisiblePosition + 1));
            }

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    if (currentVisiblePosition != currentVisibleItem) {
                        Log.i("DailyShortsAdapter", "cuevideo" + (currentVisiblePosition + 1));
                        youTubePlayer.cueVideo(video.getVideoUrl(), 0);
                    } else {
                        Log.i("DailyShortsAdapter", "loadvideo" + (currentVisiblePosition + 1));
                        youTubePlayer.loadVideo(video.getVideoUrl(), 0);
                        currentCueVideo = currentVisiblePosition;
                    }
                }

                @Override
                public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                    super.onError(youTubePlayer, error);
                    Log.i("DailyShortsAdapter", "onError" + error.toString());
                }

                @Override
                public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                    super.onStateChange(youTubePlayer, state);
                    if (state == PlayerConstants.PlayerState.PAUSED) {
                        Log.i("DailyShortsAdapter", "Paused");
                    }
                    if (state == PlayerConstants.PlayerState.ENDED) {
                        Log.i("DailyShortsAdapter", "Ended");
                        completedListener.onCompleted(video);
                    }
                }
            });
        }
    }
}
