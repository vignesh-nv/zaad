package com.zaad.zaad.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.FullVideosAdapter;
import com.zaad.zaad.adapter.YoutubeCategoryAdapter;
import com.zaad.zaad.model.Category;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.User;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.model.YoutubeCategory;
import com.zaad.zaad.viewmodel.YoutubeVideosViewModel;

import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CheckedInputStream;

public class FullYoutubeVideosActivity extends AppCompatActivity {

    RecyclerView recyclerView, categoryRecyclerview;

    String category;

    List<String> categories = new ArrayList<>();

    private YoutubeVideosViewModel youtubeVideosViewModel;

    private List<Video> videos = new ArrayList<>();

    private FullVideosAdapter fullVideosAdapter;

    private Chip entertainmentChip, newsChip, educationChip, comedyChip, trailersChip, moviesChip, cinemaChip;

    private boolean showCategory;

    private ChipGroup chipGroup;

    private String videoCategory;

    private FirebaseUser firebaseUser;

    private FirebaseFirestore firestore;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_youtube_videos);

        youtubeVideosViewModel = new ViewModelProvider(this).get(YoutubeVideosViewModel.class);

        categories = Arrays.asList("Entertainment", "News", "Education", "comedy", "Trailers", "Movies", "Cinema");
        setupChip();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        category = getIntent().getStringExtra("category");
        videoCategory = getIntent().getStringExtra("VIDEO_CATEGORY");
        showCategory = getIntent().getBooleanExtra("SHOW_CATEGORY", false);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String title = getIntent().getStringExtra("TITLE");
            if (title != null && !title.equals("")) {
                getSupportActionBar().setTitle(title);
            }
        }

//        categoryRecyclerview = findViewById(R.id.youtube_video_category_recyclerview);
        recyclerView = findViewById(R.id.full_youtube_videos_recyclerview);

        setupYoutubeCategory();
        fullVideosAdapter = new FullVideosAdapter(videos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fullVideosAdapter);

        loadUser();

        if (!showCategory) {
            chipGroup.setVisibility(View.GONE);
        }
    }

    private void loadUser() {
        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    if (videoCategory != null && !videoCategory.equals("")) {
                        loadVideos(videoCategory);
                    } else {
                        loadAllVideos();
                    }
                });
    }

    private void loadVideos(final String category) {
        firestore.collection("youtube")
                .whereEqualTo("language", user.getLanguage())
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Video> videoList = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            videoList.add(snapshot.toObject(Video.class));
                        }
                        videos.clear();
                        videos.addAll(videoList);
                        fullVideosAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void setupChip() {
        entertainmentChip = findViewById(R.id.entertainment_chip);
        comedyChip = findViewById(R.id.comedy_chip);
        trailersChip = findViewById(R.id.trailers_chip);
        cinemaChip = findViewById(R.id.cinema_chip);
        moviesChip = findViewById(R.id.movies_chip);
        newsChip = findViewById(R.id.news_chip);
        educationChip = findViewById(R.id.education_chip);
    }

    private void setupYoutubeCategory() {
//        YoutubeCategoryAdapter youtubeCategoryAdapter = new YoutubeCategoryAdapter(generateCategoryList());
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//
//        categoryRecyclerview.setLayoutManager(linearLayoutManager);
//        categoryRecyclerview.setAdapter(youtubeCategoryAdapter);
        chipGroup = findViewById(R.id.youtube_category_chipgroup);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.size() == 0) {
                loadAllVideos();
                return;
            }
            int checkChipId = chipGroup.getCheckedChipId();
            if (educationChip.getId() == checkChipId) {
                loadVideos("Education");
            } else if (comedyChip.getId() == checkChipId) {
                loadVideos("Comedy");
            } else if (newsChip.getId() == checkChipId) {
                loadVideos("News");
            } else if (entertainmentChip.getId() == checkChipId) {
                loadVideos("Entertainment");
            } else if (trailersChip.getId() == checkChipId) {
                loadVideos("Trailers");
            } else if (moviesChip.getId() == checkChipId) {
                loadVideos("Movies");
            } else if (cinemaChip.getId() == checkChipId) {
                loadVideos("Cinema");
            }
        });
    }

    private void loadAllVideos() {
        firestore.collection("youtube")
                .whereEqualTo("language", user.getLanguage())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Video> videoList = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        videoList.add(snapshot.toObject(Video.class));
                    }
                    videos.clear();
                    videos.addAll(videoList);
                    fullVideosAdapter.notifyDataSetChanged();
                });
    }
}