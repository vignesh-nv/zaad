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
import com.zaad.zaad.activity.DailyTaskVideoActivity;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.Video;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Set;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class DailyTasksAdapter extends RecyclerView.Adapter<DailyTasksAdapter.DailyTasksViewHolder> {

    private List<DailyTaskVideo> itemList;

    private Set<String> completedTasks;
    private Context context;

    public DailyTasksAdapter(List<DailyTaskVideo> itemList, Set<String> completedTasks, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.completedTasks = completedTasks;
    }

    @NonNull
    @Override
    public DailyTasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.daily_task_item,
                        viewGroup, false);

        return new DailyTasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTasksViewHolder viewHolder, int position) {

        DailyTaskVideo video = itemList.get(position);
        ImageLoader imageLoader = Coil.imageLoader(context);

        ImageRequest request = new ImageRequest.Builder(context)
                .data(video.getImageUrl())
                .crossfade(true)
                .target(viewHolder.imageView)
                .build();
        imageLoader.enqueue(request);

        if (completedTasks.contains(video.getTaskId())) {
            viewHolder.completedTxt.setVisibility(View.VISIBLE);
        }
        viewHolder.imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DailyTaskVideoActivity.class);
            intent.putExtra("TASK", itemList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class DailyTasksViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView completedTxt;
        DailyTasksViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.daily_task_image);
            completedTxt = itemView.findViewById(R.id.completed_txt);
        }
    }
}