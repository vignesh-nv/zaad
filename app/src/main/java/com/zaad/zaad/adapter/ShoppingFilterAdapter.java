package com.zaad.zaad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingFilterAdapter extends RecyclerView.Adapter<ShoppingFilterAdapter.FilterViewHolder>{

    List<String> districts;
    List<Boolean> selections;

    public ShoppingFilterAdapter(List<String> districts, List<String> selectedDistricts) {
        this.districts = districts;
        this.selections = new ArrayList<>(Collections.nCopies(districts.size(), false));


        if (selectedDistricts != null)
            for (int i = 0; i < districts.size(); i++) {
                if (selectedDistricts.contains(districts.get(i))) {
                    this.selections.set(i, true);
                }
            }
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.filter_item,
                        parent, false);

        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        String name = districts.get(position);

        holder.districtName.setText(districts.get(position));
        holder.checkBox.setChecked(selections.get(position));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> selections.set(position, isChecked));
    }

    @Override
    public int getItemCount() {
        return districts.size();
    }

    public List<String> getAllSelectedDistricts() {
        List<String> selectedDistricts = new ArrayList<>();
        for (int i = 0; i < districts.size(); i++) {
            if (selections.get(i)) {
                selectedDistricts.add(districts.get(i));
            }
        }
        return selectedDistricts;
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView districtName;

        FilterViewHolder(final View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box);
            districtName = itemView.findViewById(R.id.name_text_view);
        }
    }
}
