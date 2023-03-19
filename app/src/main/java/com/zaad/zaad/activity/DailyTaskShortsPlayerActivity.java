package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_SHORTS_COMPLETED_COUNT;
import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_VIDEO_COMPLETED_COUNT;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.DailyTaskShortsVideoAdapter;
import com.zaad.zaad.adapter.YoutubeShortsVideoAdapter;
import com.zaad.zaad.listeners.ShortsPlayCompletedListener;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.ui.dailytask.DailyTaskViewModel;
import com.zaad.zaad.viewmodel.YoutubeVideosViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DailyTaskShortsPlayerActivity extends AppCompatActivity implements ShortsPlayCompletedListener {

    DailyTaskViewModel dailyTaskViewModel;
    List<DailyTaskVideo> dailyTaskShorts = new ArrayList<>();
    ViewPager2 viewPager;
    private Set<String> completedTaskIds = new HashSet<>();
    DailyTaskShortsVideoAdapter shortsAdapter;
    DailyTaskVideo video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task_shorts_player);
        video = (DailyTaskVideo) getIntent().getSerializableExtra("TASK");

        dailyTaskViewModel = new ViewModelProvider(this).get(DailyTaskViewModel.class);
        dailyTaskShorts.add(video);
        viewPager = findViewById(R.id.view_pager);

        getUncompletedTaskVideos();
    }

    private void getUncompletedTaskVideos() {

        dailyTaskViewModel.getCompletedTasksIds().observe(this, data -> {
            completedTaskIds.addAll(data);
        });

        dailyTaskViewModel.getDailyTaskShorts().observe(this, data -> {
            dailyTaskShorts.clear();
            dailyTaskShorts.add(video);
            List<DailyTaskVideo> completedTasks = new ArrayList<>();
            for (DailyTaskVideo taskVideo: data) {
                if (!completedTaskIds.contains(taskVideo.getTaskId())) {
                    dailyTaskShorts.add(taskVideo);
                } else {
                    completedTasks.add(taskVideo);
                }
            }
            dailyTaskShorts.addAll(completedTasks);
//            shortsAdapter.notifyDataSetChanged();
            shortsAdapter = new DailyTaskShortsVideoAdapter(dailyTaskShorts, this);
            viewPager.setAdapter(shortsAdapter);
            Log.i("DailyTask", String.valueOf(dailyTaskShorts.size()));
        });
    }

    @Override
    public void onCompleted(DailyTaskVideo video) {
        if (!completedTaskIds.contains(video.getTaskId())) {
            Log.i("DailyTaskShorts Called", video.getTaskId());
            dailyTaskViewModel.insertCompletedTask(video);

            Toast.makeText(this, "Completed Task", Toast.LENGTH_SHORT).show();
            incrementViewsCount();
        }
    }

    private void incrementViewsCount() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        int videoWatched = sharedPref.getInt(DAILY_TASK_SHORTS_COMPLETED_COUNT, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (videoWatched == 24) {
            editor.putInt(DAILY_TASK_SHORTS_COMPLETED_COUNT, 0);
            editor.apply();
        } else {
            editor.putInt(DAILY_TASK_SHORTS_COMPLETED_COUNT, videoWatched + 1);
            editor.apply();
        }
    }
}