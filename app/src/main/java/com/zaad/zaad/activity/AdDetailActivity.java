package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.zaad.zaad.R;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class AdDetailActivity extends AppCompatActivity {

    ImageView adImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);

        adImage = findViewById(R.id.ad_image);
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        ImageLoader imageLoader = Coil.imageLoader(this);


        ImageRequest request = new ImageRequest.Builder(this)
                .data(imageUrl)
                .crossfade(true)
                .target(adImage)
                .build();
        imageLoader.enqueue(request);
    }
}