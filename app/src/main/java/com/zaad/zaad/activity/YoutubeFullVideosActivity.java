package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.FullVideosAdapter;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.viewmodel.YoutubeVideosViewModel;

import java.util.ArrayList;
import java.util.List;

public class YoutubeFullVideosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String collection, category;
    List<Video> videos = new ArrayList<>();

    YoutubeVideosViewModel videosViewModel;
    FullVideosAdapter videosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_full_videos);
        collection = getIntent().getStringExtra("COLLECTION");
        category = getIntent().getStringExtra("CATEGORY");

        videosViewModel = new ViewModelProvider(this).get(YoutubeVideosViewModel.class);

        recyclerView = findViewById(R.id.youtube_full_videos_recyclerview);
        videosAdapter = new FullVideosAdapter(videos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(videosAdapter);
        getVideosData();
    }

    private void getVideosData() {
        videosViewModel.getYoutubeVideosByCollectionAndCategory(collection, category).observe(this, data -> {
            videos.clear();
            videos.addAll(data);
            videosAdapter.notifyDataSetChanged();
        });
    }
}