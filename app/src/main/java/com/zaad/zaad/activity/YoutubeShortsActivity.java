package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ShortsVideoAdapter;
import com.zaad.zaad.adapter.YoutubeShortsVideoAdapter;
import com.zaad.zaad.model.Video;

import java.util.ArrayList;
import java.util.List;

public class YoutubeShortsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_shorts);

        final ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(new YoutubeShortsVideoAdapter(generateVideos()));
    }

    private List<Video> generateVideos() {
        ArrayList<Video> videos = new ArrayList<>();

        Video video = new Video();
        video.setVideoUrl("lfln6a4xh2w");

        Video video1 = new Video();
        video1.setVideoUrl("4uC47bYx1VY");

        videos.add(video);
        videos.add(video1);
        return videos;
    }

}