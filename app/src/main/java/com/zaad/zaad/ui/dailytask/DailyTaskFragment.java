package com.zaad.zaad.ui.dailytask;

import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_END_DATE_TIME;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.adapter.DailyTaskShortsAdapter;
import com.zaad.zaad.adapter.DailyTasksAdapter;
import com.zaad.zaad.databinding.FragmentDailyTaskBinding;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DailyTaskFragment extends Fragment {

    private FragmentDailyTaskBinding binding;
    private RecyclerView recyclerView, shortsRecyclerView;
    private List<DailyTaskVideo> dailyTasksList = new ArrayList<>();

    private List<DailyTaskVideo> dailyTaskShorts = new ArrayList<>();
    private Set<String> completedTaskIds = new HashSet<>();

    private TextView shortsWatchedCount, videosWatchedCount;

    private FirebaseUser firebaseUser;

    private User user;

    private DailyTaskViewModel dailyTaskViewModel;

    private DailyTasksAdapter dailyTasksAdapter;

    private DailyTaskShortsAdapter shortsAdapter;

    private FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dailyTaskViewModel =
                new ViewModelProvider(this).get(DailyTaskViewModel.class);

        binding = FragmentDailyTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = binding.dailyTaskVideosRecyclerview;
        shortsRecyclerView = binding.dailyTaskShortsRecyclerview;
        shortsWatchedCount = binding.shortsWatchedCount;
        videosWatchedCount = binding.videosWatchedCount;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager shortsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        dailyTasksAdapter = new DailyTasksAdapter(dailyTasksList, completedTaskIds, getContext());

        recyclerView.setAdapter(dailyTasksAdapter);
        recyclerView.setLayoutManager(layoutManager);

        shortsAdapter = new DailyTaskShortsAdapter(dailyTaskShorts, completedTaskIds, getContext());

        shortsRecyclerView.setAdapter(shortsAdapter);
        shortsRecyclerView.setLayoutManager(shortsLayoutManager);

        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    loadDailyTask();
                });

        loadWatchedCount();
        return root;
    }

    private void loadDailyTask() {
        dailyTaskViewModel.getCompletedTasksIds().observe(getViewLifecycleOwner(), data -> {
            completedTaskIds.addAll(data);
            dailyTasksAdapter.notifyDataSetChanged();
            shortsAdapter.notifyDataSetChanged();
        });

        dailyTaskViewModel.getDailyTaskVideos(user.getLanguage()).observe(getViewLifecycleOwner(), data -> {
            dailyTasksList.clear();
            List<DailyTaskVideo> tasks = new ArrayList<>();
            for (DailyTaskVideo video : data) {
                if (!completedTaskIds.contains(video.getTaskId())) {
                    dailyTasksList.add(video);
                } else {
                    tasks.add(video);
                }
            }
            dailyTasksList.addAll(tasks);
            dailyTasksAdapter.notifyDataSetChanged();
        });
        dailyTaskViewModel.getDailyTaskShorts(user.getLanguage()).observe(getViewLifecycleOwner(), data -> {
            dailyTaskShorts.clear();
            List<DailyTaskVideo> tasks = new ArrayList<>();
            for (DailyTaskVideo video : data) {
                if (!completedTaskIds.contains(video.getTaskId())) {
                    dailyTaskShorts.add(video);
                } else {
                    tasks.add(video);
                }
            }
            dailyTaskShorts.addAll(tasks);
            shortsAdapter.notifyDataSetChanged();
        });
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
        updateWatchedCountOnDayEnd();
        int videoWatched = sharedPreferences.getInt(DAILY_TASK_VIDEO_COMPLETED_COUNT, 0);
        int shortsWatched = sharedPreferences.getInt(DAILY_TASK_SHORTS_COMPLETED_COUNT, 0);
        videosWatchedCount.setText(String.valueOf(videoWatched));
        shortsWatchedCount.setText(String.valueOf(shortsWatched));
    }

    private void updateWatchedCountOnDayEnd() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String endDate = sharedPreferences.getString(DAILY_TASK_END_DATE_TIME, "");

        if (endDate.equals("")) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date savedDate = null;
        try {
            savedDate = dateFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date currentDate = new Date();

        if (savedDate != null && savedDate.before(currentDate)) {
            // The saved date is before the current date
            Log.i("DailyTaskFragment", "Day is before");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(DAILY_TASK_VIDEO_COMPLETED_COUNT, 0);
            editor.putInt(DAILY_TASK_SHORTS_COMPLETED_COUNT, 0);
            editor.apply();
        }
    }
}