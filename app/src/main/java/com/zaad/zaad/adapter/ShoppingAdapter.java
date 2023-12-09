package com.zaad.zaad.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.util.Log;
import com.zaad.zaad.R;
import com.zaad.zaad.listeners.OnContactClickListener;
import com.zaad.zaad.model.Shop;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ShoppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Shop> shopList;

    private Activity context;

    private String availability;

    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 132;

    OnContactClickListener onContactClickListener;

    // Constructor
    public ShoppingAdapter(List<Shop> shopList, Activity context, String availability, OnContactClickListener onContactClickListener) {
        this.shopList = shopList;
        this.context = context;
        this.availability = availability;
        this.onContactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == 0) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.shop_item,
                            viewGroup, false);

            return new ShoppingAdapter.OfflineShoppingViewHolder(view);
        }
        View view =  LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.online_shop_item,
                        viewGroup, false);

        return new ShoppingAdapter.OnlineShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        Shop shop = shopList.get(position);
        if (availability.equals("OFFLINE")) {
            ShoppingAdapter.OfflineShoppingViewHolder offlineShoppingViewHolder =
                    (ShoppingAdapter.OfflineShoppingViewHolder) viewHolder;
            offlineShoppingViewHolder.shopNameTxt.setText(shop.getName());

            ImageLoader imageLoader = Coil.imageLoader(context);
            ImageRequest request = new ImageRequest.Builder(context)
                    .data(shop.getImageUrl())
                    .crossfade(true)
                    .target(offlineShoppingViewHolder.shopImage)
                    .build();
            imageLoader.enqueue(request);
            offlineShoppingViewHolder.shopAddressTxt.setText(shop.getAddress());

            Date openingTime = shop.getOpeningTime();
            Date closingTime = shop.getClosingTime();
            Date currentTime = Calendar.getInstance().getTime();

            if (openingTime.getHours() <= currentTime.getHours() &&
                    currentTime.getHours() < closingTime.getHours()) {
                offlineShoppingViewHolder.shopStatus.setText("Open");
                offlineShoppingViewHolder.shopStatus.setTextColor(Color.GREEN);
            } else {
                offlineShoppingViewHolder.shopStatus.setText("Closed");
                offlineShoppingViewHolder.shopStatus.setTextColor(Color.RED);
            }

            offlineShoppingViewHolder.contactBtn.setOnClickListener(view -> {
                onContactClickListener.onContactClicked(shop.getPhoneNumber());
            });
            offlineShoppingViewHolder.mapBtn.setOnClickListener(view -> {
                if (shop.getMapUrl() == null) {
                    return;
                }
                Uri uri = Uri.parse(shop.getMapUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            });
        } else {
            ShoppingAdapter.OnlineShoppingViewHolder onlineShoppingViewHolder =
                    (ShoppingAdapter.OnlineShoppingViewHolder) viewHolder;
            onlineShoppingViewHolder.websiteName.setText(shop.getWebsiteName());

            ImageLoader imageLoader = Coil.imageLoader(context);
            ImageRequest request = new ImageRequest.Builder(context)
                    .data(shop.getImageUrl())
                    .crossfade(true)
                    .target(onlineShoppingViewHolder.productImage)
                    .build();
            imageLoader.enqueue(request);

            onlineShoppingViewHolder.productName.setText(shop.getName());
            onlineShoppingViewHolder.priceTxt.setText("Rs. " + shop.getPrice());
            onlineShoppingViewHolder.ratingTxt.setText("Rating: " + shop.getRating());
            onlineShoppingViewHolder.buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shop.getBuyLink() == null || shop.getBuyLink().equals("") || !shop.getBuyLink().startsWith("https:")) {
                        return;
                    }
                    Uri uri = Uri.parse(shop.getBuyLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (availability.equals("OFFLINE")) {
            return 0;
        }
        return 1;
    }

    class OfflineShoppingViewHolder extends RecyclerView.ViewHolder {
        ImageView shopImage;
        TextView shopNameTxt, shopAddressTxt, shopStatus;

        Button contactBtn, mapBtn;
        View addressLayout, timingLayout;

        OfflineShoppingViewHolder(View itemView) {
            super(itemView);
            shopNameTxt = itemView.findViewById(R.id.shopNameTxt);
            shopAddressTxt = itemView.findViewById(R.id.shop_address);
            shopImage = itemView.findViewById(R.id.shopImage);
            shopStatus = itemView.findViewById(R.id.shopStatus);
            contactBtn = itemView.findViewById(R.id.contact_btn);
            mapBtn = itemView.findViewById(R.id.map_btn);
            addressLayout = itemView.findViewById(R.id.addressLayout);
            timingLayout = itemView.findViewById(R.id.timingLayout);
        }
    }

    class OnlineShoppingViewHolder extends RecyclerView.ViewHolder {

        TextView websiteName, productName, priceTxt, ratingTxt;
        ImageView productImage;

        Button buyBtn;

        public OnlineShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            websiteName = itemView.findViewById(R.id.websiteName);
            productName = itemView.findViewById(R.id.productName);
            productImage = itemView.findViewById(R.id.productImage);
            priceTxt = itemView.findViewById(R.id.price_txt);
            buyBtn = itemView.findViewById(R.id.buy_link);
            ratingTxt = itemView.findViewById(R.id.rating_txt);
        }
    }


}
