package com.zaad.zaad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.zaad.zaad.R;
import com.zaad.zaad.model.YoutubeCategory;

import java.util.List;

public class YoutubeCategoryAdapter extends RecyclerView.Adapter<YoutubeCategoryAdapter.YoutubeChipViewHolder> {

    private List<YoutubeCategory> youtubeCategoryList;

    public YoutubeCategoryAdapter(List<YoutubeCategory> categories) {
        this.youtubeCategoryList = categories;
    }

    @NonNull
    @Override
    public YoutubeChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.youtube_category_chip,
                        parent, false);

        return new YoutubeChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeChipViewHolder holder, int position) {
        holder.chip.setText(youtubeCategoryList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return youtubeCategoryList.size();
    }

    class YoutubeChipViewHolder extends RecyclerView.ViewHolder {

        Chip chip;
        public YoutubeChipViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.category_chip);
        }
    }
}
