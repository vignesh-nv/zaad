package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewTreeViewModelKt;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.VideoView;

import com.zaad.zaad.R;

public class VideoPlayerActivity extends AppCompatActivity {

    WebView webView;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoView = findViewById(R.id.videoView);
        String url = getIntent().getStringExtra("VIDEO_URL");
        videoView.setVideoPath(url);
        videoView.start();
//        webView = findViewById(R.id.web_view);
//        final WebSettings settings = webView.getSettings();
//        settings.setDefaultTextEncodingName("utf-8"); settings.setJavaScriptEnabled(true);
//        webView.loadUrl("https://www.facebook.com/video/embed?video_id=1298152510547483");
    }
}