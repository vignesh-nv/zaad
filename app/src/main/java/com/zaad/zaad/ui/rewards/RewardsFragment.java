package com.zaad.zaad.ui.rewards;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import com.zaad.zaad.R;
import com.zaad.zaad.activity.CouponsActivity;
import com.zaad.zaad.adapter.AvailableCouponsAdapter;
import com.zaad.zaad.adapter.CouponsAdapter;
import com.zaad.zaad.adapter.RedeemedCouponsAdapter;
import com.zaad.zaad.fragment.CouponSelectFragment;
import com.zaad.zaad.listeners.CouponOnClickListener;
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.model.User;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class RewardsFragment extends Fragment implements CouponOnClickListener {

    private RewardsViewModel mViewModel;

    private RecyclerView availableCouponsRecyclerView, redeemedCouponsRecyclerView;

    private RedeemedCouponsAdapter redeemedCouponsAdapter;

    private List<Coupon> couponList = new ArrayList<>();

    private User user = new User();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_rewards, container, false);
        availableCouponsRecyclerView = view.findViewById(R.id.available_coupons_recyclerView);
        redeemedCouponsRecyclerView = view.findViewById(R.id.redeemed_coupons_recyclerView);

        mViewModel = new ViewModelProvider(getActivity()).get(RewardsViewModel.class);

        mViewModel.getUser().observe(getActivity(), data -> {
            user = data;
        });

        loadData();
        setupAvailableCouponsRecyclerView();
        return view;
    }

    private void loadData() {
        redeemedCouponsAdapter = new RedeemedCouponsAdapter(couponList, getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);

        redeemedCouponsRecyclerView.setLayoutManager(layoutManager);
        redeemedCouponsRecyclerView.setHasFixedSize(true);
        redeemedCouponsRecyclerView.setAdapter(redeemedCouponsAdapter);

        mViewModel.getMyCoupons().observe(getActivity(), data -> {
            couponList.clear();
            couponList.addAll(data);
            redeemedCouponsAdapter.notifyDataSetChanged();
        });
    }

    private void setupAvailableCouponsRecyclerView() {
        AvailableCouponsAdapter availableCouponsAdapter = new AvailableCouponsAdapter(4, getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        availableCouponsRecyclerView.setLayoutManager(layoutManager);
        availableCouponsRecyclerView.setHasFixedSize(true);
        availableCouponsRecyclerView.setAdapter(availableCouponsAdapter);

    }

    @Override
    public void onclick(Coupon coupon) {
        CouponSelectFragment couponSelectFragment = new CouponSelectFragment();
        couponSelectFragment.show(getFragmentManager(), " CouponSelectFragment");
    }
}
