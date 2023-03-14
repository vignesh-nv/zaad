package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.chip.ChipGroup;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.CouponsAdapter;
import com.zaad.zaad.listeners.CouponOnClickListener;
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.viewmodel.CouponsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CouponsActivity extends AppCompatActivity implements CouponOnClickListener {

    RecyclerView couponsRecyclerview;
    CouponsAdapter couponsAdapter;
    CouponsViewModel couponsViewModel;
    List<Coupon> couponList = new ArrayList<>();
    private List<String> onlineCouponsCategory = new ArrayList<>();
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        category = getIntent().getStringExtra("CATEGORY");

        onlineCouponsCategory = Arrays.asList("", "");
        couponsRecyclerview = findViewById(R.id.coupons_recyclerView);
        couponsViewModel = new ViewModelProvider(this).get(CouponsViewModel.class);
        setupUI();
        setupCategory();
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

    private void setupCategory() {
        ChipGroup chipGroup = findViewById(R.id.online_coupons_category);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.size() == 0) {
                loadAllCoupons();
                return;
            }
            loadCoupons(onlineCouponsCategory.get(checkedIds.get(0)-1));
        });
    }

    @Override
    public void onclick(Coupon coupon) {
        couponsViewModel.redeemCoupon(coupon);
        couponsViewModel.decrementAvailableCoupons();
        finish();
    }

    private void loadAllCoupons() {
        couponsViewModel.getCoupons().observe(this, data -> {
            couponList.clear();
            couponList.addAll(data);
        });
    }

    private void loadCoupons(final String couponsCategory) {
        couponsViewModel.getOnlineCouponsByCategory(couponsCategory).observe(this, data -> {
            couponList.clear();
            couponList.addAll(data);
        });
    }
}
