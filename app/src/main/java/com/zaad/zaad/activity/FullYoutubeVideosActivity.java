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
    private List<Video> videos;

    private FullVideosAdapter fullVideosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_youtube_videos);

        youtubeVideosViewModel = new ViewModelProvider(this).get(YoutubeVideosViewModel.class);

        categories = Arrays.asList("Entertainment", "News", "Education", "comedy", "Music");

        category = getIntent().getStringExtra("category");
        Log.i("FullVideosActivity", "Category " + category);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        categoryRecyclerview = findViewById(R.id.youtube_video_category_recyclerview);
        recyclerView = findViewById(R.id.full_youtube_videos_recyclerview);

        setupYoutubeCategory();
        videos = generateList();
        fullVideosAdapter = new FullVideosAdapter(videos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fullVideosAdapter);
    }

    private void loadVideos(final String category) {
        youtubeVideosViewModel.getYoutubeVideosByCategory(category).observe(this, data -> {
            videos.clear();
            videos.addAll(data);
            fullVideosAdapter.notifyDataSetChanged();
        });
    }
    private void setupYoutubeCategory() {
//        YoutubeCategoryAdapter youtubeCategoryAdapter = new YoutubeCategoryAdapter(generateCategoryList());
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//
//        categoryRecyclerview.setLayoutManager(linearLayoutManager);
//        categoryRecyclerview.setAdapter(youtubeCategoryAdapter);
        ChipGroup chipGroup = findViewById(R.id.youtube_category_chipgroup);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            Toast.makeText(FullYoutubeVideosActivity.this, checkedIds.toString(), Toast.LENGTH_SHORT).show();
            if (checkedIds.size() == 0) {
                loadAllVideos();
                return;
            }
            loadVideos(categories.get(checkedIds.get(0)-1));
        });

//        Chip chip = new Chip(this);
//        chip.setText("Entertainment");
//        chip.setId(ViewCompat.generateViewId());
//
//        Chip chip1 = new Chip(this);
//        chip1.setText("Comedy");
//        chip1.setId(ViewCompat.generateViewId());
//
//        Chip chip2 = new Chip(this);
//        chip2.setText("News");
//        chip2.setId(ViewCompat.generateViewId());
//
//        Chip chip3 = new Chip(this);
//        chip3.setText("Education");
//        chip3.setId(ViewCompat.generateViewId());
//
//        Chip chip4 = new Chip(this);
//        chip4.setText("Sports");
//        chip4.setId(ViewCompat.generateViewId());
//
//        chipGroup.addView(chip);
//        chipGroup.addView(chip1);
//        chipGroup.addView(chip2);
//        chipGroup.addView(chip3);
//        chipGroup.addView(chip4);
    }

    private void loadAllVideos() {
        videos.clear();
        videos.addAll(generateList());
        fullVideosAdapter.notifyDataSetChanged();
    }

    private List<Video> generateList() {

        Video video = new Video();
        video.setTitle("Video");
        video.setImageUrl("https://i.ytimg.com/vi/Q38f4frs8yc/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLB3puC9Pn8mtlYQp9gClJ83Z1Fq7Q");
        video.setVideoUrl("Q38f4frs8yc");

        Video video1 = new Video();
        video1.setTitle("Video");
        video1.setImageUrl("https://i.ytimg.com/vi/5BQQM4uvRkw/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLAn1LHxREk816KdBcSjVbxpkITWbA");
        video1.setVideoUrl("5BQQM4uvRkw");

        Video video2 = new Video();
        video2.setTitle("Video");
        video2.setImageUrl("https://i.ytimg.com/vi/SFdGyt0V00M/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLCbSJZ7wGrNOhf5Kwu06lH-lO8Fgg");
        video2.setVideoUrl("");

        List<Video> videoList = new ArrayList<>();
        videoList.add(video);
        videoList.add(video1);
        videoList.add(video2);
        videoList.add(video2);
        videoList.add(video2);

        return videoList;
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