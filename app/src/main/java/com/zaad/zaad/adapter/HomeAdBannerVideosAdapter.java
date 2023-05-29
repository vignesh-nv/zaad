package com.zaad.zaad.adapter;

import static android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ALL;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.zaad.zaad.R;
import com.zaad.zaad.cache.AppCache;
import com.zaad.zaad.listeners.AdCompleteListener;
import com.zaad.zaad.model.AdBanner;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class HomeAdBannerVideosAdapter extends RecyclerView.Adapter<HomeAdBannerVideosAdapter.VideoViewHolder> {

    private List<AdBanner> itemList;
    private Context context;

    private AdCompleteListener listener;

    private int mCurrentPosition = -1;
    private SimpleCache simpleCache;
    HttpDataSource.Factory httpDataSourceFactory;
    DataSource.Factory cacheDataSourceFactory;

    public HomeAdBannerVideosAdapter(List<AdBanner> itemList, Context context, AdCompleteListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
//        simpleCache = AppCache.getSimpleCache(context);
//        httpDataSourceFactory = new DefaultHttpDataSource.Factory();
//        cacheDataSourceFactory =
//                new CacheDataSource.Factory()
//                        .setCache(simpleCache)
//                        .setUpstreamDataSourceFactory(httpDataSourceFactory)
//                        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.home_ad_banner,
                        viewGroup, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder viewHolder, int position) {

        AdBanner adBanner = itemList.get(position);
        ImageLoader imageLoader = Coil.imageLoader(context);

        ImageRequest request = new ImageRequest.Builder(context)
                .data(adBanner.getImageUrl())
                .crossfade(true)
                .target(viewHolder.imageView)
                .build();
        imageLoader.enqueue(request);
        viewHolder.currentAdNumber.setText(String.valueOf(position + 1));
        viewHolder.totalAdCount.setText("/" + itemList.size());

        viewHolder.title.setText(adBanner.getTitle());

        viewHolder.videoView.setVideoPath(adBanner.getVideoUrl());
        viewHolder.videoView.start();

        viewHolder.videoView.setOnCompletionListener(mediaPlayer -> {
            listener.onComplete(position);
        });

        viewHolder.videoView.setOnPreparedListener(mediaPlayer -> {
            viewHolder.videoView.seekTo(0);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(0, 0);
            mediaPlayer.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mediaPlayer.start();
            viewHolder.videoView.start();
            viewHolder.imageView.setVisibility(View.GONE);

            float videoRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
            float screenRatio = viewHolder.videoView.getWidth() / (float) viewHolder.videoView.getHeight();
            float scaleX = videoRatio / screenRatio;
            if (scaleX >= 1f) {
                viewHolder.videoView.setScaleX(scaleX);
            } else {
                viewHolder.videoView.setScaleY(1f / scaleX);
            }

            mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                listener.onComplete(position);
            });
        });

//        Player.Listener listener1 = new Player.Listener() {
//            @Override
//            public void onPlaybackStateChanged(int playbackState) {
//                Player.Listener.super.onPlaybackStateChanged(playbackState);
//                Toast.makeText(context, "State: " + playbackState, Toast.LENGTH_SHORT).show();
//                if (playbackState == Player.STATE_ENDED) {
//                    Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show();
//                    listener.onComplete(position);
//                }
//            }
//        };
//        ExoPlayer player = new ExoPlayer.Builder(context)
//                .setMediaSourceFactory(
//                        new DefaultMediaSourceFactory(context)
//                                .setDataSourceFactory(cacheDataSourceFactory))
//                .build();
//        player.addListener(listener1);
//        viewHolder.styledPlayerView.setPlayer(player);
//        Uri videoUri = Uri.parse(adBanner.getVideoUrl());
//        MediaItem mediaItem = MediaItem.fromUri(videoUri);
//        MediaSource mediaSource =
//                new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem);
////        player.setMediaItem(new MediaItem.Builder().setUri(adBanner.getVideoUrl()).build());
//        player.addMediaSource(mediaSource);
//        player.prepare();
//        player.setRepeatMode(REPEAT_MODE_ALL);
//        player.setPlayWhenReady(true);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        //        private VideoView videoView;
        private TextView totalAdCount, currentAdNumber, title;
        StyledPlayerView styledPlayerView;

        VideoView videoView;

        VideoViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ad_image);
//            videoView = itemView.findViewById(R.id.ad_video);
            totalAdCount = itemView.findViewById(R.id.total_ad_count);
            currentAdNumber = itemView.findViewById(R.id.current_ad_number);
            title = itemView.findViewById(R.id.ad_title);
//            styledPlayerView = itemView.findViewById(R.id.ad_video);
            videoView = itemView.findViewById(R.id.ad_video);
        }
    }
}
