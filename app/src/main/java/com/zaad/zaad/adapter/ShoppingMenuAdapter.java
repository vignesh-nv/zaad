package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.ShoppingMenuActivity;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.ShoppingMenu;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ShoppingMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<ShoppingMenu> itemList;
    private Context context;

    public ShoppingMenuAdapter(List<ShoppingMenu> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == 10) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.home_ad_item,
                            viewGroup, false);

            return new ImageAd(view);
        }
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.home_item,
                        viewGroup, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int type = viewHolder.getItemViewType();
        ShoppingMenu item = itemList.get(position);
        if (type == 10) {
            ImageAd imageAd = (ImageAd) viewHolder;
            ImageLoader imageLoader = Coil.imageLoader(context);

            ImageRequest request = new ImageRequest.Builder(context)
                    .data(item.getImageUrl())
                    .crossfade(true)
                    .target(imageAd.imageView)
                    .build();
            imageLoader.enqueue(request);

            imageAd.imageView.setOnClickListener(view -> {
                if (item.getLink() == null || item.getLink().equals("") || !item.getLink().startsWith("https:")) {
                    return;
                }
                Uri uri = Uri.parse(item.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            });
            return;
        }
        VideoViewHolder videoViewHolder = (VideoViewHolder) viewHolder;

        videoViewHolder.title.setText(item.getTitle());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false);
        layoutManager.setInitialPrefetchItemCount(item.getShoppingMenuItemList().size());
        ShoppingItemAdapter childItemAdapter = new ShoppingItemAdapter(item.getShoppingMenuItemList(), context, item.getAvailability());

        videoViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
        videoViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
        videoViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);

        videoViewHolder.moreBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShoppingMenuActivity.class);
            intent.putExtra("SHOP_TYPE", item.getAvailability());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ShoppingMenu shoppingMenu = itemList.get(position);
        if (shoppingMenu.getImageUrl()!=null) {
            return 10;
        }
        return super.getItemViewType(position);
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

    class ImageAd extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ImageAd(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_child_item);
        }
    }
}