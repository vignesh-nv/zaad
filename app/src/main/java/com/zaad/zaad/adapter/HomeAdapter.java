package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.activity.FullVideosActivity;
import com.zaad.zaad.R;
import com.zaad.zaad.VideoType;
import com.zaad.zaad.activity.FullYoutubeVideosActivity;
import com.zaad.zaad.activity.ShoppingTimeActivity;
import com.zaad.zaad.model.HomeItem;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<HomeItem> itemList;
    private Context context;

    public HomeAdapter(List<HomeItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.home_item,
                        viewGroup, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        int type = viewHolder.getItemViewType();

        if (type == 0 || type == 1 || type == 2) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) viewHolder;
            HomeItem parentItem = itemList.get(position);
            videoViewHolder.title.setText(parentItem.getTitle());
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                    false);
            layoutManager.setInitialPrefetchItemCount(parentItem.getVideos().size());
            HomeItemAdapter childItemAdapter;
            if (type == 0) {
                childItemAdapter = new HomeItemAdapter(parentItem.getVideos(), context, VideoType.YOUTUBE_VIDEO.name());
            } else if (type == 1) {
                videoViewHolder.moreBtn.setVisibility(View.INVISIBLE);
                childItemAdapter = new HomeItemAdapter(parentItem.getVideos(), context, VideoType.YOUTUBE_SHORTS.name());
            } else {
                videoViewHolder.moreBtn.setVisibility(View.INVISIBLE);
                childItemAdapter = new HomeItemAdapter(parentItem.getVideos(), context, VideoType.INSTAGRAM_REEL.name());
            }
            videoViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
            videoViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
            videoViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
            videoViewHolder.moreBtn.setOnClickListener(view -> {
                Intent intent = new Intent(context, FullYoutubeVideosActivity.class);
                intent.putExtra("category", parentItem.getCategory());
                context.startActivity(intent);
            });
        } else if (type == 5) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) viewHolder;
            HomeItem parentItem = itemList.get(position);
            videoViewHolder.title.setText(parentItem.getTitle());
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                    false);
            layoutManager.setInitialPrefetchItemCount(parentItem.getVideos().size());
            HomeItemAdapter childItemAdapter;
            childItemAdapter = new HomeItemAdapter(parentItem.getVideos(), context, VideoType.FACEBOOK_VIDEOS.name());
            videoViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
            videoViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
            videoViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
            videoViewHolder.moreBtn.setOnClickListener(view -> {
                Intent intent = new Intent(context, FullVideosActivity.class);
                intent.putExtra("category", parentItem.getCategory());
                context.startActivity(intent);
            });
        } else if (type == 3) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) viewHolder;
            HomeItem parentItem = itemList.get(position);
            videoViewHolder.title.setText(parentItem.getTitle());
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                    false);
            layoutManager.setInitialPrefetchItemCount(parentItem.getVideos().size());
            HomeItemAdapter childItemAdapter = new HomeItemAdapter(parentItem.getVideos(), context, parentItem.getCategory());
            videoViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
            videoViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
            videoViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
            videoViewHolder.moreBtn.setOnClickListener(view -> {
                Intent intent = new Intent(context, ShoppingTimeActivity.class);
                intent.putExtra("SHOP_TYPE", parentItem.getCategory());
                context.startActivity(intent);
            });
        } else if (type == 4) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) viewHolder;
            HomeItem parentItem = itemList.get(position);
            videoViewHolder.title.setText(parentItem.getTitle());
            videoViewHolder.moreBtn.setVisibility(View.INVISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                    false);
            layoutManager.setInitialPrefetchItemCount(parentItem.getVideos().size());
            HomeItemAdapter childItemAdapter = new HomeItemAdapter(parentItem.getVideos(), context, parentItem.getCategory());
            videoViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
            videoViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
            videoViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
            videoViewHolder.moreBtn.setOnClickListener(view -> {
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        HomeItem parentItem = itemList.get(position);

        String category = parentItem.getCategory();
        if (category.equals(VideoType.YOUTUBE_VIDEO.name())) {
            return 0;
        } else if (category.equals(VideoType.YOUTUBE_SHORTS.name())) {
            return 1;
        } else if (category.equals(VideoType.INSTAGRAM_REEL.name())) {
            return 2;
        } else if (category.equals("ONLINE_STORE") || category.equals("OFFLINE_STORE")) {
            return 3;
        } else if (category.equals(VideoType.IMAGE_AD.name())) {
            return 4;
        } else if (category.equals(VideoType.FACEBOOK_VIDEOS.name())) {
            return 5;
        }
        return 6;
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
