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

//    private Chip entertainmentChip, cookingChip, unboxingChip, movieReviewChip, gamingChip, travelChip, comedyChip;
//    private Chip foodReviewChip, cinemaTalksChip, trailerChip, motivationChip, newsChip, liveChip, businessTalksChip;

    private boolean showCategory;
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
//        setupChip();

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

//        setupYoutubeCategory();
        fullVideosAdapter = new FullVideosAdapter(videos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fullVideosAdapter);

        loadUser();
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

//    private void setupChip() {
//        entertainmentChip = findViewById(R.id.entertainment_chip);
//        cookingChip = findViewById(R.id.cooking_chip);
//        unboxingChip = findViewById(R.id.unboxing_chip);
//        movieReviewChip = findViewById(R.id.movie_review_chip);
//        gamingChip = findViewById(R.id.gaming_chip);
//        travelChip = findViewById(R.id.travel_chip);
//        foodReviewChip = findViewById(R.id.food_review_chip);
//        cinemaTalksChip = findViewById(R.id.cinema_talks_chip);
//        trailerChip = findViewById(R.id.trailer_chip);
//        motivationChip = findViewById(R.id.motivation_chip);
//        newsChip = findViewById(R.id.news_chip);
//        liveChip = findViewById(R.id.live_chip);
//        businessTalksChip = findViewById(R.id.business_talks_chip);
//        comedyChip = findViewById(R.id.comedy_chip);
//    }

//    private void setupYoutubeCategory() {
////        YoutubeCategoryAdapter youtubeCategoryAdapter = new YoutubeCategoryAdapter(generateCategoryList());
////        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
////
////        categoryRecyclerview.setLayoutManager(linearLayoutManager);
////        categoryRecyclerview.setAdapter(youtubeCategoryAdapter);
//        chipGroup = findViewById(R.id.youtube_category_chipgroup);
//        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
//            if (checkedIds.size() == 0) {
//                loadAllVideos();
//                return;
//            }
//            int checkChipId = chipGroup.getCheckedChipId();
//            if (entertainmentChip.getId() == checkChipId) {
//                loadVideos("Entertainment");
//            } else if (cookingChip.getId() == checkChipId) {
//                loadVideos("Cooking");
//            } else if (unboxingChip.getId() == checkChipId) {
//                loadVideos("Unboxing");
//            } else if (movieReviewChip.getId() == checkChipId) {
//                loadVideos("MovieReview");
//            } else if (gamingChip.getId() == checkChipId) {
//                loadVideos("Gaming");
//            } else if (travelChip.getId() == checkChipId) {
//                loadVideos("Travel");
//            } else if (foodReviewChip.getId() == checkChipId) {
//                loadVideos("FoodReview");
//            } else if (cinemaTalksChip.getId() == checkChipId) {
//                loadVideos("CinemaTalks");
//            } else if (trailerChip.getId() == checkChipId) {
//                loadVideos("Trailer");
//            } else if (motivationChip.getId() == checkChipId) {
//                loadVideos("Motivation");
//            } else if (newsChip.getId() == checkChipId) {
//                loadVideos("News");
//            } else if (liveChip.getId() == checkChipId) {
//                loadVideos("Live");
//            } else if (businessTalksChip.getId() == checkChipId) {
//                loadVideos("BusinessTalks");
//            } else if (comedyChip.getId() == checkChipId) {
//                loadVideos("Comedy");
//            }
//        });
//    }

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