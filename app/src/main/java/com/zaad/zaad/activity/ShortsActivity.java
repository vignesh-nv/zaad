package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ShortsVideoAdapter;
import com.zaad.zaad.model.Video;

import java.util.ArrayList;
import java.util.List;

public class ShortsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shorts);

        final ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(new ShortsVideoAdapter(generateVideos()));
    }

    private List<Video> generateVideos() {
        ArrayList<Video> videos = new ArrayList<>();

        Video video = new Video();
        video.setVideoUrl("https://static.videezy.com/system/resources/previews/000/044/479/original/banana.mp4");

        Video video1 = new Video();
        video1.setVideoUrl("https://static.videezy.com/system/resources/previews/000/043/977/original/DSC_8447_V1-0010.mp4");

        Video video2 = new Video();
        video2.setVideoUrl("https://static.videezy.com/system/resources/previews/000/044/479/original/banana.mp4");

        Video video3 = new Video();
        video3.setVideoUrl("https://static.videezy.com/system/resources/previews/000/000/168/original/Record.mp4");

        videos.add(video);
        videos.add(video1);
        videos.add(video2);
        videos.add(video3);
        return videos;
    }
}