package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ShortsVideoAdapter;
import com.zaad.zaad.adapter.YoutubeShortsVideoAdapter;
import com.zaad.zaad.listeners.ShortsPlayCompletedListener;
import com.zaad.zaad.model.Video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YoutubeShortsActivity extends AppCompatActivity {

    String videoUrl;
    FirebaseFirestore firestore;
    List<Video> shorts = new ArrayList<>();
    ViewPager2 viewPager2;

    YoutubeShortsVideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_shorts);

        firestore = FirebaseFirestore.getInstance();
        videoUrl = getIntent().getStringExtra("VIDEO_URL");
        viewPager2 = findViewById(R.id.view_pager);
        Video video = new Video();
        video.setVideoUrl(videoUrl);
        shorts.add(video);
        videoAdapter = new YoutubeShortsVideoAdapter(shorts);
        viewPager2.setAdapter(videoAdapter);
        generateVideos();
    }

    private void generateVideos() {
        firestore.collection("youtubeShorts").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Video> shortsList = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Video video = snapshot.toObject(Video.class);
                        if (video.getVideoUrl().equals(videoUrl)) {
                            continue;
                        }
                        shortsList.add(snapshot.toObject(Video.class));
                    }
                    Collections.sort(shortsList);
                    shorts.addAll(shortsList);
                    videoAdapter.notifyDataSetChanged();
                });
    }
}