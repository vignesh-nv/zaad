package com.zaad.zaad.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.util.Log;
import com.zaad.zaad.R;
import com.zaad.zaad.model.Shop;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ShoppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Shop> shopList;

    private Context context;

    // Constructor
    public ShoppingAdapter(List<Shop> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.shop_item,
                        viewGroup, false);

        return new ShoppingAdapter.OnlineShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        Shop shop = shopList.get(position);
        ShoppingAdapter.OnlineShoppingViewHolder onlineShoppingViewHolder =
                (ShoppingAdapter.OnlineShoppingViewHolder) viewHolder;
        onlineShoppingViewHolder.shopNameTxt.setText(shop.getName());

        ImageLoader imageLoader = Coil.imageLoader(context);
        ImageRequest request = new ImageRequest.Builder(context)
                .data(shop.getImageUrl())
                .crossfade(true)
                .target(onlineShoppingViewHolder.shopImage)
                .build();
        imageLoader.enqueue(request);

        if (shop.getAvailability().equals("OFFLINE")) {
            onlineShoppingViewHolder.buyLink.setVisibility(View.GONE);
            onlineShoppingViewHolder.onlineAddressTxt.setVisibility(View.GONE);
            onlineShoppingViewHolder.onlineAddressTxt.setText(shop.getAddress());
            onlineShoppingViewHolder.shopContactTxt.setText(shop.getPhoneNumber());
            onlineShoppingViewHolder.shopAddressTxt.setText(shop.getAddress());
            onlineShoppingViewHolder.buyLinkLayout.setVisibility(View.GONE);

            int openingTime = shop.getOpeningTime().getHours();
            int closingTime = shop.getClosingTime().getHours();
            int currentTime = Calendar.getInstance().getTime().getHours();

            Log.i("ShoppingAdapter", openingTime + " " + closingTime + " " + currentTime);
            if (openingTime <= currentTime && currentTime <= closingTime) {
                onlineShoppingViewHolder.shopStatus.setText("Open");
                onlineShoppingViewHolder.shopStatus.setTextColor(Color.GREEN);

            } else {
                onlineShoppingViewHolder.shopStatus.setText("Closed");
                onlineShoppingViewHolder.shopStatus.setTextColor(Color.RED);
            }
        } else {
            onlineShoppingViewHolder.shopAddressTxt.setVisibility(View.GONE);
            onlineShoppingViewHolder.shopContactTxt.setVisibility(View.GONE);
            onlineShoppingViewHolder.contactLayout.setVisibility(View.GONE);
            onlineShoppingViewHolder.addressLayout.setVisibility(View.GONE);
            onlineShoppingViewHolder.timingLayout.setVisibility(View.GONE);
            onlineShoppingViewHolder.onlineAddressTxt.setVisibility(View.VISIBLE);

            onlineShoppingViewHolder.buyLink.setText(shop.getBuyLink());
            onlineShoppingViewHolder.onlineAddressTxt.setText(shop.getWebsiteName());
            onlineShoppingViewHolder.shopAddressTxt.setText(shop.getAddress());
            onlineShoppingViewHolder.shopContactTxt.setText(shop.getPhoneNumber());
        }
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    class OnlineShoppingViewHolder extends RecyclerView.ViewHolder {
        ImageView shopImage;
        TextView shopNameTxt, shopContactTxt, buyLink, onlineAddressTxt, shopAddressTxt, shopStatus, websiteName;

        View buyLinkLayout, addressLayout, contactLayout, timingLayout;

        OnlineShoppingViewHolder(View itemView) {
            super(itemView);
            shopNameTxt = itemView.findViewById(R.id.shopNameTxt);
            shopContactTxt = itemView.findViewById(R.id.shop_contact_number);
            buyLink = itemView.findViewById(R.id.buyLink);
            onlineAddressTxt = itemView.findViewById(R.id.onlineAddressTxt);
            shopAddressTxt = itemView.findViewById(R.id.shop_address);
            shopImage = itemView.findViewById(R.id.shopImage);
            shopStatus = itemView.findViewById(R.id.shopStatus);

            buyLinkLayout = itemView.findViewById(R.id.buyLinkLayout);
            addressLayout = itemView.findViewById(R.id.addressLayout);
            contactLayout = itemView.findViewById(R.id.contactLayout);
            timingLayout = itemView.findViewById(R.id.timingLayout);
        }
    }
}
