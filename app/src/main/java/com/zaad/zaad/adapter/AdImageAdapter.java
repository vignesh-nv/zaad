package com.zaad.zaad.adapter;

import static android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.zaad.zaad.R;
import com.zaad.zaad.listeners.AdCompleteListener;
import com.zaad.zaad.model.AdBanner;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class AdImageAdapter extends RecyclerView.Adapter<AdImageAdapter.VideoViewHolder> {

    private List<AdBanner> itemList;
    private Context context;

    private AdCompleteListener listener;

    public AdImageAdapter(List<AdBanner> itemList, Context context, AdCompleteListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;

    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.image_ad,
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
//        viewHolder.currentAdNumber.setText(String.valueOf(position + 1));
//        viewHolder.totalAdCount.setText("/" + itemList.size());

//        viewHolder.videoView.setOnCompletionListener(mediaPlayer -> {
//            listener.onComplete(position);
//        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
//        private TextView totalAdCount, currentAdNumber, title;

        VideoView videoView;

        VideoViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ad_image);
//            totalAdCount = itemView.findViewById(R.id.total_ad_count);
//            currentAdNumber = itemView.findViewById(R.id.current_ad_number);
//            title = itemView.findViewById(R.id.ad_title);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView totalAdCount, currentAdNumber, title;

        ImageViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ad_image);
//            totalAdCount = itemView.findViewById(R.id.total_ad_count);
//            currentAdNumber = itemView.findViewById(R.id.current_ad_number);
//            title = itemView.findViewById(R.id.ad_title);
        }
    }
}
