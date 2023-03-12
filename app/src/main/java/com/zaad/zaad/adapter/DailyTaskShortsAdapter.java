package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.DailyTaskShortsPlayerActivity;
import com.zaad.zaad.activity.DailyTaskVideoActivity;
import com.zaad.zaad.model.DailyTaskVideo;

import java.util.List;
import java.util.Set;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class DailyTaskShortsAdapter extends RecyclerView.Adapter<DailyTaskShortsAdapter.DailyTaskShortsViewHolder> {

    private List<DailyTaskVideo> itemList;

    private Set<String> completedTasks;
    private Context context;

    public DailyTaskShortsAdapter(List<DailyTaskVideo> itemList, Set<String> completedTasks, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.completedTasks = completedTasks;
    }

    @NonNull
    @Override
    public DailyTaskShortsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.daily_task_shorts_item,
                        viewGroup, false);

        return new DailyTaskShortsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTaskShortsViewHolder viewHolder, int position) {

        DailyTaskVideo video = itemList.get(position);
        ImageLoader imageLoader = Coil.imageLoader(context);

        ImageRequest request = new ImageRequest.Builder(context)
                .data(video.getImageUrl())
                .crossfade(true)
                .target(viewHolder.imageView)
                .build();
        imageLoader.enqueue(request);

//        if (completedTasks.contains(video.getTaskId())) {
//            viewHolder.completedTxt.setVisibility(View.VISIBLE);
//        }
        viewHolder.imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DailyTaskShortsPlayerActivity.class);
            intent.putExtra("TASK", itemList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class DailyTaskShortsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        DailyTaskShortsViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.short_imageview);
        }
    }
}