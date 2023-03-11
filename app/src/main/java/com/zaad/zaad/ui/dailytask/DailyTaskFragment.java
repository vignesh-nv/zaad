package com.zaad.zaad.ui.dailytask;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.adapter.ChildVideosAdapter;
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
    private RecyclerView recyclerView;
    private List<DailyTaskVideo> dailyTasksList = new ArrayList<>();
    private Set<String> completedTaskIds = new HashSet<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DailyTaskViewModel dailyTaskViewModel =
                new ViewModelProvider(this).get(DailyTaskViewModel.class);

        binding = FragmentDailyTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.dailyTaskRecyclerview;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        DailyTasksAdapter dailyTasksAdapter = new DailyTasksAdapter(dailyTasksList, completedTaskIds,  getContext());

        recyclerView.setAdapter(dailyTasksAdapter);
        recyclerView.setLayoutManager(layoutManager);

        dailyTaskViewModel.getCompletedTasksIds().observe(getViewLifecycleOwner(), data -> {
            Log.i("DailyTaskFragment", data.toString());
            completedTaskIds.addAll(data);
            dailyTasksAdapter.notifyDataSetChanged();

        });

        dailyTaskViewModel.getDailyTaskVideos().observe(getViewLifecycleOwner(), data -> {
            dailyTasksList.clear();
            dailyTasksList.addAll(data);
            dailyTasksAdapter.notifyDataSetChanged();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}