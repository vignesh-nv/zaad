package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.CouponsAdapter;
import com.zaad.zaad.adapter.FullVideosAdapter;
import com.zaad.zaad.listeners.CouponOnClickListener;
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.viewmodel.CouponsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CouponsActivity extends AppCompatActivity implements CouponOnClickListener {

    RecyclerView couponsRecyclerview;
    CouponsAdapter couponsAdapter;
    CouponsViewModel couponsViewModel;

    List<Coupon> couponList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        couponsRecyclerview = findViewById(R.id.coupons_recyclerView);
        couponsViewModel = new ViewModelProvider(this).get(CouponsViewModel.class);
        setupUI();
    }

    private void setupUI() {
        couponsAdapter = new CouponsAdapter(couponList, this, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        couponsRecyclerview.setLayoutManager(layoutManager);
        couponsRecyclerview.setHasFixedSize(true);
        couponsRecyclerview.setAdapter(couponsAdapter);

        couponsViewModel.getCoupons().observe(this, data -> {
            couponList.clear();
            couponList.addAll(data);
            couponsAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onclick(Coupon coupon) {
        couponsViewModel.redeemCoupon(coupon);
        finish();
    }
}
