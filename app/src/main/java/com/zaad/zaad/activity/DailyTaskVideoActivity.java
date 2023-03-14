package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_VIDEO_COMPLETED_COUNT;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.DailyTaskSuggestionVideoAdapter;
import com.zaad.zaad.listeners.DailyTaskSuggestionVideoClickListener;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.ui.dailytask.DailyTaskViewModel;
import com.zaad.zaad.utils.DailyTaskYoutubePlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DailyTaskVideoActivity extends AppCompatActivity implements DailyTaskSuggestionVideoClickListener {

    YouTubePlayerView youTubePlayerView;
    private DailyTaskVideo dailyTaskVideo;

    private DailyTaskViewModel dailyTaskViewModel;

    private List<DailyTaskVideo> uncompletedTasks = new ArrayList<>();

    private Set<String> completedTaskIds = new HashSet<>();

    private List<DailyTaskVideo> dailyTaskVideos = new ArrayList<>();

    private RecyclerView recyclerView;

    DailyTaskSuggestionVideoAdapter suggestionVideoAdapter;

    float currentSecond = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task_video);

        dailyTaskVideo = (DailyTaskVideo) getIntent().getSerializableExtra("TASK");

        dailyTaskViewModel = new ViewModelProvider(this).get(DailyTaskViewModel.class);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        loadYoutubePlayer();
        getUncompletedTaskVideos();
        loadSuggestedVideos();
        dailyTaskViewModel.getWatchedSeconds(dailyTaskVideo.getTaskId()).observe(this, data -> {
            if (data != null){
                currentSecond = data;
                Log.i("DailyTaskVideo S:", String.valueOf(currentSecond));
            } else {
                Log.i("DailyTaskVideo S:", "Null Value");
            }
        });
    }

    private void loadYoutubePlayer() {
        getLifecycle().addObserver(youTubePlayerView);

        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                DailyTaskYoutubePlayer defaultPlayerUiController = new DailyTaskYoutubePlayer(youTubePlayerView, youTubePlayer);
                youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        dailyTaskVideo.getVideoUrl(),
                        currentSecond
                );
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                if (state == PlayerConstants.PlayerState.ENDED) {
                    if (!completedTaskIds.contains(dailyTaskVideo.getTaskId())) {
                        incrementViewsCount();
                        dailyTaskViewModel.insertCompletedTask(dailyTaskVideo);
                        Toast.makeText(DailyTaskVideoActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                currentSecond = second;
            }
        };

        // disable web ui
        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();

        youTubePlayerView.initialize(listener, options);
    }
    private void loadSuggestedVideos() {
        recyclerView = findViewById(R.id.daily_task_suggestion_task_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        suggestionVideoAdapter = new DailyTaskSuggestionVideoAdapter(dailyTaskVideos, this, this);
        recyclerView.setAdapter(suggestionVideoAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getUncompletedTaskVideos() {

        dailyTaskViewModel.getCompletedTasksIds().observe(this, data -> {
            completedTaskIds.addAll(data);
        });

        dailyTaskViewModel.getDailyTaskVideos().observe(this, data -> {
            uncompletedTasks.clear();
            uncompletedTasks.addAll(data);

            dailyTaskVideos.clear();
            for (DailyTaskVideo task : uncompletedTasks) {
                if (!completedTaskIds.contains(task.getTaskId()) && !task.getTaskId().equals(dailyTaskVideo.getTaskId())) {
                    dailyTaskVideos.add(task);
                }
            }
            suggestionVideoAdapter.notifyDataSetChanged();
        });
    }

    private void incrementViewsCount() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        int videoWatched = sharedPref.getInt(DAILY_TASK_VIDEO_COMPLETED_COUNT, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (videoWatched == 9) {
            editor.putInt(DAILY_TASK_VIDEO_COMPLETED_COUNT, 0);
            editor.apply();
            incrementAvailableCoupons();
            Toast.makeText(this, "You won a coupon!!!", Toast.LENGTH_SHORT).show();
        } else {
            editor.putInt(DAILY_TASK_VIDEO_COMPLETED_COUNT, videoWatched + 1);
            editor.apply();
        }
    }

    private void incrementAvailableCoupons() {
        dailyTaskViewModel.incrementAvailableCoupons();
    }

    @Override
    public void onClick(DailyTaskVideo video) {
        Intent intent = new Intent(this, DailyTaskVideoActivity.class);
        intent.putExtra("VIDEO_ID", video.getVideoUrl());
        intent.putExtra("TASK", video);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("DailyTaskVideo", "ActivityPaused");
        dailyTaskViewModel.updateWatchedSeconds(dailyTaskVideo.getTaskId(), currentSecond);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("DailyTaskVideo", "ActivityResumed");
    }
}