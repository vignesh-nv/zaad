package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.FullVideosAdapter;
import com.zaad.zaad.model.Video;

import java.util.ArrayList;
import java.util.List;

public class FullVideosActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_videos);

        category = getIntent().getStringExtra("category");
        Log.i("FullVideosActivity", "Category " + category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Trending Videos");

        recyclerView = findViewById(R.id.full_videos_recyclerview);
        FullVideosAdapter videosAdapter = new FullVideosAdapter(generateList(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(videosAdapter);
    }

    private List<Video> generateList() {

        Video video = new Video();
        video.setTitle("Video");
        video.setImageUrl("https://i.ytimg.com/vi/F0CIXNJplhY/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLCzq65sft_XLnJfqyWIwnfRLweNJA");

        Video video1 = new Video();
        video1.setTitle("Video");
        video1.setImageUrl("https://i.ytimg.com/vi/joW_Bcv-iSw/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLDMcGUt6_G_ybjL3ugHv2QxPcRPKQ");

        Video video2 = new Video();
        video2.setTitle("Video");
        video2.setImageUrl("https://i.ytimg.com/vi/SFdGyt0V00M/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLCbSJZ7wGrNOhf5Kwu06lH-lO8Fgg");

        List<Video> videoList = new ArrayList<>();
        videoList.add(video);
        videoList.add(video1);
        videoList.add(video2);
        videoList.add(video2);
        videoList.add(video2);

        return videoList;
    }

}