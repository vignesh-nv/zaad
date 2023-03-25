package com.zaad.zaad.activity;

import androidx.annotation.NonNull;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.FullVideosAdapter;
import com.zaad.zaad.adapter.YoutubeCategoryAdapter;
import com.zaad.zaad.model.Category;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.model.YoutubeCategory;
import com.zaad.zaad.viewmodel.YoutubeVideosViewModel;

import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FullYoutubeVideosActivity extends AppCompatActivity {

    RecyclerView recyclerView, categoryRecyclerview;

    String category;

    List<String> categories = new ArrayList<>();

    private YoutubeVideosViewModel youtubeVideosViewModel;

    private List<Video> videos = new ArrayList<>();

    private FullVideosAdapter fullVideosAdapter;

    private Chip entertainmentChip, newsChip, educationChip, comedyChip, musicChip;

    private boolean showCategory;

    private ChipGroup chipGroup;

    private String videoCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_youtube_videos);

        youtubeVideosViewModel = new ViewModelProvider(this).get(YoutubeVideosViewModel.class);

        categories = Arrays.asList("Entertainment", "News", "Education", "comedy", "Music");
        setupChip();

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

        if (videoCategory != null && !videoCategory.equals("")) {
            loadVideos(videoCategory);
        } else {
            loadAllVideos();
        }

        if (!showCategory) {
            chipGroup.setVisibility(View.GONE);
        }
    }

    private void loadVideos(final String category) {
        youtubeVideosViewModel.getYoutubeVideosByCategory(category).observe(this, data -> {
            videos.clear();
            videos.addAll(data);
            fullVideosAdapter.notifyDataSetChanged();
        });
    }

    private void setupChip() {
        entertainmentChip = findViewById(R.id.entertainment_chip);
        comedyChip = findViewById(R.id.comedy_chip);
        musicChip = findViewById(R.id.music_chip);
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
            } else if (musicChip.getId() == checkChipId) {
                loadVideos("Music");
            }
        });
    }

    private void loadAllVideos() {
        youtubeVideosViewModel.getAllYoutubeVideos().observe(this, data -> {
            videos.clear();
            videos.addAll(data);
            fullVideosAdapter.notifyDataSetChanged();
        });
    }

    private List<YoutubeCategory> generateCategoryList() {
        List<YoutubeCategory> categories = new ArrayList<>();
        YoutubeCategory category1 = new YoutubeCategory();
        category1.setTitle("Entertainment");

        YoutubeCategory category2 = new YoutubeCategory();
        category2.setTitle("Education");

        YoutubeCategory category3 = new YoutubeCategory();
        category3.setTitle("Comedy");

        YoutubeCategory category4 = new YoutubeCategory();
        category4.setTitle("News");

        YoutubeCategory category5 = new YoutubeCategory();
        category5.setTitle("Trailers");

        YoutubeCategory category6 = new YoutubeCategory();
        category6.setTitle("Movies");

        YoutubeCategory category7 = new YoutubeCategory();
        category7.setTitle("Cinema");

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        categories.add(category4);
        categories.add(category5);
        categories.add(category6);
        categories.add(category7);

        return categories;
    }
}