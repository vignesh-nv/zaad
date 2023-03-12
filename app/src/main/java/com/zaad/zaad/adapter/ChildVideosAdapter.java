package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.VideoType;
import com.zaad.zaad.activity.ChildVideoPlayerActivity;
import com.zaad.zaad.activity.FullVideosActivity;
import com.zaad.zaad.activity.YoutubeFullVideosActivity;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.Video;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ChildVideosAdapter extends RecyclerView.Adapter<ChildVideosAdapter.VideoViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<HomeItem> itemList;
    private Context context;

    public ChildVideosAdapter(List<HomeItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.home_item,
                        viewGroup, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder viewHolder, int position) {

        HomeItem item = itemList.get(position);

        viewHolder.title.setText(item.getTitle());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false);
        layoutManager.setInitialPrefetchItemCount(item.getVideos().size());
        HomeItemAdapter childItemAdapter = new HomeItemAdapter(item.getVideos(), context, VideoType.YOUTUBE_VIDEO.name());

        viewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
        viewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
        viewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
        viewHolder.moreBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, YoutubeFullVideosActivity.class);
            intent.putExtra("CATEGORY", item.getCategory());
            intent.putExtra("COLLECTION", "music");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private RecyclerView ChildRecyclerView;

        private Button moreBtn;

        VideoViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.parent_item_title);
            ChildRecyclerView = itemView.findViewById(R.id.item_recyclerView);
            moreBtn = itemView.findViewById(R.id.more_button);
        }
    }
}