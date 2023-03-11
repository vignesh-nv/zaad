package com.zaad.zaad.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.model.Shop;
import com.zaad.zaad.model.Video;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        if (shop.getAvailability().equals("OFFLINE")) {
            onlineShoppingViewHolder.buyLink.setVisibility(View.GONE);
            onlineShoppingViewHolder.onlineAddressTxt.setVisibility(View.GONE);
            onlineShoppingViewHolder.onlineAddressTxt.setText(shop.getAddress());
            onlineShoppingViewHolder.buyLinkLayout.setVisibility(View.GONE);

            Date openingTime = shop.getOpeningTime();
            Date closingTime = shop.getClosingTime();
            Date currentTime = Calendar.getInstance().getTime();
            if (currentTime.after(openingTime) && currentTime.before(closingTime)) {
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
        TextView shopNameTxt, shopContactTxt, buyLink, onlineAddressTxt, shopAddressTxt, shopStatus;

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

