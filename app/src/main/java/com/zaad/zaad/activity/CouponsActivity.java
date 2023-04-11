package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.CouponsAdapter;
import com.zaad.zaad.listeners.CouponOnClickListener;
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.viewmodel.CouponsViewModel;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CouponsActivity extends AppCompatActivity implements CouponOnClickListener {

    RecyclerView couponsRecyclerview;
    CouponsAdapter couponsAdapter;
    CouponsViewModel couponsViewModel;
    List<Coupon> couponList = new ArrayList<>();
    private List<String> onlineCouponsCategory = new ArrayList<>();

    private String category, availability;
    ChipGroup chipGroup;

    Chip fashionChip, electronicsChip, foodChip, ecommerceChip, furnitureChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        availability = getIntent().getStringExtra("CATEGORY");

        onlineCouponsCategory = Arrays.asList("", "");
        couponsRecyclerview = findViewById(R.id.coupons_recyclerView);
        couponsViewModel = new ViewModelProvider(this).get(CouponsViewModel.class);

        fashionChip = findViewById(R.id.fashionChip);
        electronicsChip = findViewById(R.id.electronicsChip);
        foodChip = findViewById(R.id.foodChip);
        ecommerceChip = findViewById(R.id.ecommerceChip);
        furnitureChip = findViewById(R.id.furnitureChip);
        chipGroup = findViewById(R.id.online_coupons_category);

        setupUI();
        if (availability.equals("ONLINE"))
            setupCategory();
        else
            chipGroup.setVisibility(View.GONE);
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
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.size() == 0) {
                loadAllCoupons();
                return;
            }
            int checkChipId = chipGroup.getCheckedChipId();
            if (fashionChip.getId() == checkChipId) {
                loadCoupons("Fashion");
            } else if (electronicsChip.getId() == checkChipId) {
                loadCoupons("Electronics");
            } else if (foodChip.getId() == checkChipId) {
                loadCoupons("Food");
            } else if (ecommerceChip.getId() == checkChipId) {
                loadCoupons("Ecommerce");
            } else if (furnitureChip.getId() == checkChipId) {
                loadCoupons("Furniture");
            }
        });
    }

    @Override
    public void onclick(Coupon coupon) {
        if (coupon.getAvailability().equals("OFFLINE")) {
            int codeLength = 6;
            char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new SecureRandom();
            for (int i = 0; i < codeLength; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            String couponCode = sb.toString();
            coupon.setCouponCode(couponCode);
        }
        couponsViewModel.redeemCoupon(coupon);
        couponsViewModel.decrementAvailableCoupons();
        finish();
    }

    private void loadAllCoupons() {
        couponsViewModel.getCoupons().observe(this, data -> {
            couponList.clear();
            couponList.addAll(data);
            couponsAdapter.notifyDataSetChanged();
        });
    }

    private void loadCoupons(final String couponsCategory) {
        couponsViewModel.getOnlineCouponsByCategory(couponsCategory).observe(this, data -> {
            couponList.clear();
            couponList.addAll(data);
            couponsAdapter.notifyDataSetChanged();
        });
    }
}
