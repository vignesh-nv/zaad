package com.zaad.zaad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.AdImageAdapter;
import com.zaad.zaad.adapter.YoutubeSuggestionVideosAdapter;
import com.zaad.zaad.listeners.AdCompleteListener;
import com.zaad.zaad.listeners.OnSuggestionVideoClick;
import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.utils.YoutubePlayerController;
import com.zaad.zaad.viewmodel.YoutubeVideosViewModel;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class YoutubeVideoPlayerActivity extends AppCompatActivity implements OnSuggestionVideoClick,
        AdCompleteListener {

    String youtubeVideoID;

    YouTubePlayerView youTubePlayerView;
    RecyclerView suggestionRecyclerview;

    Timer timer;

    FirebaseFirestore firestore;

    private YoutubeVideosViewModel youtubeVideosViewModel;

    private List<Video> suggestionVideos = new ArrayList<>();
    private YoutubeSuggestionVideosAdapter youtubeSuggestionVideosAdapter;

    ViewPager2 adSlider;

    Video video;

    List<AdBanner> ads;
    AdImageAdapter adAdapter;

    int currentAd = 0;

    Button fullVideoBtn, subscribeBtn;

    ImageView imageAdBannerView;

    AdBanner imageAdBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_player);

        youtubeVideosViewModel = new ViewModelProvider(this).get(YoutubeVideosViewModel.class);

        ads = new ArrayList<>();
        video = (Video) getIntent().getSerializableExtra("VIDEO");
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        suggestionRecyclerview = findViewById(R.id.suggestion_recyclerview);
        fullVideoBtn = findViewById(R.id.full_video_btn);
        subscribeBtn = findViewById(R.id.subscribe_btn);
        imageAdBannerView = findViewById(R.id.image_ad_banner);

        adSlider = findViewById(R.id.ad_slider);

        firestore = FirebaseFirestore.getInstance();
        initYouTubePlayerView();

        youtubeSuggestionVideosAdapter = new YoutubeSuggestionVideosAdapter(suggestionVideos, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        suggestionRecyclerview.setAdapter(youtubeSuggestionVideosAdapter);
        suggestionRecyclerview.setLayoutManager(layoutManager);
        loadAds();
        adAdapter = new AdImageAdapter(ads, this, this);
        adSlider.setAdapter(adAdapter);
        setupSuggestionVideos();
        subscribeBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse(video.getChannelUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        fullVideoBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse(video.getFullVideoUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        if (video.getBannerId() != null && !video.getBannerId().equals("")) {
            firestore.collection("imageBanner").document(video.getBannerId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        imageAdBanner = documentSnapshot.toObject(AdBanner.class);

                        ImageLoader imageLoader = Coil.imageLoader(YoutubeVideoPlayerActivity.this);
                        ImageRequest request = new ImageRequest.Builder(YoutubeVideoPlayerActivity.this)
                                .data(imageAdBanner.getImageUrl())
                                .crossfade(true)
                                .target(imageAdBannerView)
                                .build();
                        imageLoader.enqueue(request);
                    });
        }

        imageAdBannerView.setOnClickListener(view -> {
            if (imageAdBanner == null || imageAdBanner.getLink().equals("")) {
                return;
            }
            Uri uri = Uri.parse(imageAdBanner.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    private void initYouTubePlayerView() {
        getLifecycle().addObserver(youTubePlayerView);

        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YoutubePlayerController defaultPlayerUiController = new YoutubePlayerController(youTubePlayerView, youTubePlayer);
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
                        video.getVideoUrl(),
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
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.exitFullScreen();
            youtubeSuggestionVideosAdapter.notifyDataSetChanged();
            Log.i("YoutubeVideoPlayer", String.valueOf(suggestionVideos.size()));
        }
    }

    private void setupSuggestionVideos() {
        youtubeVideosViewModel.getYoutubeVideosByCategory(video.getCategory()).observe(this, data -> {
            suggestionVideos.clear();
            data.remove(video);
            suggestionVideos.addAll(data);
            youtubeSuggestionVideosAdapter.notifyDataSetChanged();
            Log.i("YoutubeVideoPlayer", "Loading videos");
        });
    }

    @Override
    public void onClick(Video video) {
        Intent intent = new Intent(this, YoutubeVideoPlayerActivity.class);
        intent.putExtra("VIDEO", video);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("YoutubeVideoPlayer", "OnResumeCalled");
    }

    private void loadAds() {
        if (video.getAdIds() == null || video.getAdIds().size() == 0) {
            return;
        }
        firestore.collection("imageBanner").whereIn("id", video.getAdIds())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        ads.add(snapshot.toObject(AdBanner.class));
                    }
                    slideAdImages();
                    adAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Log.d("YoutubeVideoPlayerActi", "onFailure: " + e.toString())
                );
    }

    private void slideAdImages() {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, 5 * 1000); //
    }

    @Override
    public void onComplete(int position) {

    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(() -> {

                if (currentAd > ads.size()) {
                    currentAd = 0;
                }
                adSlider.setCurrentItem(currentAd++);
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
    }
}