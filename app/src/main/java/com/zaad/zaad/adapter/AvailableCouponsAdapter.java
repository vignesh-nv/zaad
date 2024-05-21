package com.zaad.zaad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.listeners.CouponOnClickListener;
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.model.User;

public class AvailableCouponsAdapter extends RecyclerView.Adapter<AvailableCouponsAdapter.AvailableCouponsViewHolder> {
    private Context context;
    private int availableCouponsCount;

    private User user;
    private CouponOnClickListener onClickListener;

    public AvailableCouponsAdapter(User user, Context context, CouponOnClickListener onClickListener) {
        this.user = user;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AvailableCouponsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.available_coupons_item,
                        viewGroup, false);

        return new AvailableCouponsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableCouponsViewHolder viewHolder, int position) {

        viewHolder.imageView.setOnClickListener(view -> Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show());

        if (position % 3 == 0) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.coupon));
        } else if (position % 2 == 0) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.coupon1));
        } else {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.coupon2));
        }

        viewHolder.imageView.setOnClickListener(view -> {
            onClickListener.onclick(new Coupon());
        });
    }

    @Override
    public int getItemCount() {
        return user.getAvailableCoupons();
    }

    class AvailableCouponsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        AvailableCouponsViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.coupon_imageview);
        }
    }
}