package com.zaad.zaad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.listeners.CouponOnClickListener;
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.model.Video;

import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.CouponsViewHolder> {
    private List<Coupon> itemList;
    private Context context;

    private CouponOnClickListener listener;
    public CouponsAdapter(List<Coupon> itemList, Context context, CouponOnClickListener onClickListener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = onClickListener;
    }

    @NonNull
    @Override
    public CouponsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.coupons_item,
                        viewGroup, false);

        return new CouponsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponsViewHolder viewHolder, int position) {

        Coupon coupon = itemList.get(position);
        ImageLoader imageLoader = Coil.imageLoader(context);

        viewHolder.imageView.setOnClickListener(view -> Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show());

        viewHolder.redeemBtn.setOnClickListener(view -> {
            listener.onclick(coupon);
        });
        ImageRequest request = new ImageRequest.Builder(context)
                .data(coupon.getImageUrl())
                .crossfade(true)
                .target(viewHolder.imageView)
                .build();
        imageLoader.enqueue(request);
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