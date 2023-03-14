package com.zaad.zaad.ui.dailytask;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_SHORTS_COMPLETED_COUNT;
import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_VIDEO_COMPLETED_COUNT;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.adapter.ChildVideosAdapter;
import com.zaad.zaad.adapter.DailyTaskShortsAdapter;
import com.zaad.zaad.adapter.DailyTasksAdapter;
import com.zaad.zaad.databinding.FragmentDailyTaskBinding;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.Video;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DailyTaskFragment extends Fragment {

    private FragmentDailyTaskBinding binding;
    private RecyclerView recyclerView, shortsRecyclerView;
    private List<DailyTaskVideo> dailyTasksList = new ArrayList<>();
    private Set<String> completedTaskIds = new HashSet<>();

    private TextView shortsWatchedCount, videosWatchedCount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DailyTaskViewModel dailyTaskViewModel =
                new ViewModelProvider(this).get(DailyTaskViewModel.class);

        binding = FragmentDailyTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.dailyTaskVideosRecyclerview;
        shortsRecyclerView = binding.dailyTaskShortsRecyclerview;
        shortsWatchedCount = binding.shortsWatchedCount;
        videosWatchedCount = binding.videosWatchedCount;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager shortsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        DailyTasksAdapter dailyTasksAdapter = new DailyTasksAdapter(dailyTasksList, completedTaskIds,  getContext());

        recyclerView.setAdapter(dailyTasksAdapter);
        recyclerView.setLayoutManager(layoutManager);

        DailyTaskShortsAdapter shortsAdapter = new DailyTaskShortsAdapter(dailyTasksList, completedTaskIds, getContext());

        shortsRecyclerView.setAdapter(shortsAdapter);
        shortsRecyclerView.setLayoutManager(shortsLayoutManager);

        dailyTaskViewModel.getCompletedTasksIds().observe(getViewLifecycleOwner(), data -> {
            Log.i("DailyTaskFragment", data.toString());
            completedTaskIds.addAll(data);
            dailyTasksAdapter.notifyDataSetChanged();
            shortsAdapter.notifyDataSetChanged();
        });

        dailyTaskViewModel.getDailyTaskVideos().observe(getViewLifecycleOwner(), data -> {
            dailyTasksList.clear();
            List<DailyTaskVideo> tasks = new ArrayList<>();
            for (DailyTaskVideo video: data) {
                if (!completedTaskIds.contains(video.getTaskId())) {
                    dailyTasksList.add(video);
                } else {
                    tasks.add(video);
                }
            }
            dailyTasksList.addAll(tasks);

            dailyTasksAdapter.notifyDataSetChanged();
            shortsAdapter.notifyDataSetChanged();
        });
        loadWatchedCount();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWatchedCount();
    }

    private void loadWatchedCount() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        int videoWatched = sharedPreferences.getInt(DAILY_TASK_VIDEO_COMPLETED_COUNT, 0);
        int shortsWatched = sharedPreferences.getInt(DAILY_TASK_SHORTS_COMPLETED_COUNT, 0);
        videosWatchedCount.setText(String.valueOf(videoWatched));
        shortsWatchedCount.setText(String.valueOf(shortsWatched));
    }
}