package com.zaad.zaad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.YoutubeSuggestionVideosAdapter;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.viewmodel.YoutubeVideosViewModel;

import java.util.ArrayList;
import java.util.List;

public class YoutubeVideoPlayerActivity extends AppCompatActivity {

    String youtubeVideoID;

    YouTubePlayerView youTubePlayerView;
    RecyclerView suggestionRecyclerview;

    private YoutubeVideosViewModel youtubeVideosViewModel;

    private List<Video> suggestionVideos = new ArrayList<>();
    YoutubeSuggestionVideosAdapter youtubeSuggestionVideosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_player);

        youtubeVideosViewModel = new ViewModelProvider(this).get(YoutubeVideosViewModel.class);

        youtubeVideoID = this.getIntent().getStringExtra("VIDEO_ID");
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        suggestionRecyclerview = findViewById(R.id.suggestion_recyclerview);

        initYouTubePlayerView();


        youtubeSuggestionVideosAdapter = new YoutubeSuggestionVideosAdapter(suggestionVideos, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        suggestionRecyclerview.setAdapter(youtubeSuggestionVideosAdapter);
        suggestionRecyclerview.setLayoutManager(layoutManager);

        setupSuggestionVideos();
    }

    private void initYouTubePlayerView() {
        getLifecycle().addObserver(youTubePlayerView);

        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(youTubePlayerView, youTubePlayer);
                youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        youtubeVideoID,
                        0f
                );
            }
        };

        // disable web ui
        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();

        youTubePlayerView.initialize(listener, options);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            youTubePlayerView.exitFullScreen();
        }
    }

    private void setupSuggestionVideos() {
        youtubeVideosViewModel.getYoutubeVideosByCategory("comedy").observe(this, data -> {
            suggestionVideos.clear();
            suggestionVideos.addAll(data);
            youtubeSuggestionVideosAdapter.notifyDataSetChanged();
        });
    }
}