package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_END_DATE_TIME;
import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_VIDEO_COMPLETED_COUNT;
import static com.zaad.zaad.constants.AppConstant.SHOW_REWARDS_BADGE;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.User;
import com.zaad.zaad.ui.dailytask.DailyTaskViewModel;
import com.zaad.zaad.utils.DailyTaskYoutubePlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    User user;

    Button fullVideoBtn, subscribeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task_video);

        dailyTaskVideo = (DailyTaskVideo) getIntent().getSerializableExtra("TASK");

        dailyTaskViewModel = new ViewModelProvider(this).get(DailyTaskViewModel.class);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        fullVideoBtn = findViewById(R.id.full_video_btn);
        subscribeBtn = findViewById(R.id.subscibe_btn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        loadYoutubePlayer();
        loadSuggestedVideos();

        subscribeBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse(dailyTaskVideo.getChannelUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        fullVideoBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse(dailyTaskVideo.getFullVideoUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    getUncompletedTaskVideos();
                });
        dailyTaskViewModel.getWatchedSeconds(dailyTaskVideo.getTaskId()).observe(this, data -> {
            if (data != null) {
                currentSecond = data;
                Log.i("DailyTaskVideo S:", String.valueOf(currentSecond));
            } else {
                Log.i("DailyTaskVideo S:", "Null Value");
            }
        });

//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }

    private void loadYoutubePlayer() {
        getLifecycle().addObserver(youTubePlayerView);

        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                DailyTaskYoutubePlayer defaultPlayerUiController = new DailyTaskYoutubePlayer(youTubePlayerView, youTubePlayer);
                defaultPlayerUiController.setFullScreenButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (getResources().getConfiguration().orientation) {
                            case Configuration.ORIENTATION_LANDSCAPE:
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                break;
                            case Configuration.ORIENTATION_PORTRAIT:
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                break;
                        }
                    }
                });
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

        dailyTaskViewModel.getDailyTaskVideos(user.getLanguage()).observe(this, data -> {
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

        if (dailyTaskVideo.getCouponId() != null && !dailyTaskVideo.getCouponId().equals("")) {
            Toast.makeText(this, "You won a special coupon", Toast.LENGTH_SHORT).show();
            editor.putBoolean(SHOW_REWARDS_BADGE, true);
            editor.apply();
            firestore.collection("coupons").document(dailyTaskVideo.getCouponId()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Coupon coupon = documentSnapshot.toObject(Coupon.class);
                        dailyTaskViewModel.addCoupon(coupon);
                    });
        }
        if (videoWatched == 9) {
            editor.putInt(DAILY_TASK_VIDEO_COMPLETED_COUNT, 0);
            editor.putBoolean(SHOW_REWARDS_BADGE, true);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dateString = sdf.format(dailyTaskVideo.getExpiryDate());
            editor.putString(DAILY_TASK_END_DATE_TIME, dateString);
            editor.apply();
            incrementAvailableCoupons();
            Toast.makeText(this, "You won a coupon!!!", Toast.LENGTH_SHORT).show();
        } else {
            editor.putInt(DAILY_TASK_VIDEO_COMPLETED_COUNT, videoWatched + 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dateString = sdf.format(dailyTaskVideo.getExpiryDate());
            editor.putString(DAILY_TASK_END_DATE_TIME, dateString);
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.exitFullScreen();
        }
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