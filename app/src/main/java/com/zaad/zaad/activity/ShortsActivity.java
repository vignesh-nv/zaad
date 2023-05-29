package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ShortsVideoAdapter;
import com.zaad.zaad.model.User;
import com.zaad.zaad.model.Video;

import java.util.ArrayList;
import java.util.List;

public class ShortsActivity extends AppCompatActivity {

    private Video video;
    private List<Video> videos = new ArrayList<>();

    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;

    private String collection;

    ShortsVideoAdapter shortsVideoAdapter;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shorts);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        video = (Video) getIntent().getSerializableExtra("VIDEO");

        collection = getIntent().getStringExtra("COLLECTION");
        final ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        shortsVideoAdapter = new ShortsVideoAdapter(videos);
        viewPager2.setAdapter(shortsVideoAdapter);
        loadUser();
    }

    private void loadUser() {
        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    getVideos();
                });
    }

    private void getVideos() {
        firestore.collection(collection)
                .whereEqualTo("language", user.getLanguage())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Video> videoList = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        videoList.add(snapshot.toObject(Video.class));
                    }
                    videos.clear();
                    videos.addAll(videoList);
                    shortsVideoAdapter.notifyDataSetChanged();
                });
    }
}