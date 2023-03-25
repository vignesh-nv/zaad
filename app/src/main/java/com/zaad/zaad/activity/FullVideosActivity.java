package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.FullVideosAdapter;
import com.zaad.zaad.model.Video;

import java.util.ArrayList;
import java.util.List;

public class FullVideosActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String category;

    FirebaseFirestore firestore;

    private String collection;

    private List<Video> videoList = new ArrayList<>();
    private FullVideosAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_videos);

        category = getIntent().getStringExtra("category");
        collection = getIntent().getStringExtra("COLLECTION");

        String title = getIntent().getStringExtra("TITLE");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
        recyclerView = findViewById(R.id.full_videos_recyclerview);
        adapter = new FullVideosAdapter(videoList, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        loadVideos();
    }


    private void loadVideos() {
        firestore.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Video> videos = new ArrayList<>();
            for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                videos.add(snapshot.toObject(Video.class));
            }
            videoList.addAll(videos);
            adapter.notifyDataSetChanged();
        });
    }
}