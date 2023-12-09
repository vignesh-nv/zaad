package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.ShoppingTimeActivity;
import com.zaad.zaad.model.ShoppingMenuItem;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ShoppingItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShoppingMenuItem> itemList;
    private Context context;

    private String availability;

    public ShoppingItemAdapter(List<ShoppingMenuItem> itemList, Context context, String availability) {
        this.itemList = itemList;
        this.context = context;
        this.availability = availability;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.home_store_item,
                        viewGroup, false);

        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        ShoppingMenuItem childItem = itemList.get(position);

        ImageLoader imageLoader = Coil.imageLoader(context);
        StoreViewHolder storeViewHolder = (StoreViewHolder) viewHolder;
        ImageRequest request = new ImageRequest.Builder(context)
                .data(childItem.getImageUrl())
                .crossfade(true)
                .target(storeViewHolder.imageView)
                .build();
        imageLoader.enqueue(request);

        ((StoreViewHolder) viewHolder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShoppingTimeActivity.class);
                intent.putExtra("SHOP_TYPE", availability);
                intent.putExtra("CATEGORY", childItem.getCategory());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class StoreViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        StoreViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.store_image);
        }
    }

}

