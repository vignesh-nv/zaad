package com.zaad.zaad.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.CouponDetailsActivity;
import com.zaad.zaad.listeners.CouponOnClickListener;
import com.zaad.zaad.model.Coupon;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.disk.DiskCache;
import coil.disk.RealDiskCache;
import coil.memory.MemoryCache;
import coil.request.ImageRequest;

public class RedeemedCouponsAdapter extends RecyclerView.Adapter<RedeemedCouponsAdapter.CouponsViewHolder> {
    private List<Coupon> itemList;
    private Context context;

    public RedeemedCouponsAdapter(List<Coupon> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CouponsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.redeemed_coupons_item,
                        viewGroup, false);

        return new CouponsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponsViewHolder viewHolder, int position) {

        Coupon coupon = itemList.get(position);
        ImageLoader imageLoader = Coil.imageLoader(context).newBuilder()
                .memoryCache(new MemoryCache.Builder(context).maxSizePercent(0.25).build())
                .diskCache(new DiskCache.Builder().directory(context.getCacheDir()).maxSizePercent(0.25).build())
                .build();

        viewHolder.imageView.setOnClickListener(view -> Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show());

        ImageRequest request = new ImageRequest.Builder(context)
                .data(coupon.getImageUrl())
                .crossfade(true)
                .target(viewHolder.imageView)
                .build();
        imageLoader.enqueue(request);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CouponDetailsActivity.class);
                intent.putExtra("COUPON", coupon);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class CouponsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private Button redeemBtn;

        CouponsViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.coupon_imageview);
            redeemBtn = itemView.findViewById(R.id.redeem_button);
        }
    }
}