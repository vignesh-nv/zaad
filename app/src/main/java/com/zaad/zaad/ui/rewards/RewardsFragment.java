package com.zaad.zaad.ui.rewards;

import static com.zaad.zaad.constants.AppConstant.DAILY_TASK_VIDEO_COMPLETED_COUNT;
import static com.zaad.zaad.constants.AppConstant.SHOW_REWARDS_BADGE;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RewardsFragment extends Fragment implements CouponOnClickListener {

    private RewardsViewModel mViewModel;

    private RecyclerView availableCouponsRecyclerView, redeemedCouponsRecyclerView;

    private RedeemedCouponsAdapter redeemedCouponsAdapter;

    private List<Coupon> couponList = new ArrayList<>();

    private User user = new User();

    private Integer availableCoupons;

    AvailableCouponsAdapter availableCouponsAdapter;

    TextView noAvailableCouponsTxt, noRedeemedCouponsTxt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_rewards, container, false);
        availableCouponsRecyclerView = view.findViewById(R.id.available_coupons_recyclerView);
        redeemedCouponsRecyclerView = view.findViewById(R.id.redeemed_coupons_recyclerView);

        noAvailableCouponsTxt = view.findViewById(R.id.no_coupons_available_text);
        noRedeemedCouponsTxt = view.findViewById(R.id.no_redeemed_coupons_txt);

        mViewModel = new ViewModelProvider(getActivity()).get(RewardsViewModel.class);

        setupAvailableCouponsRecyclerView();

        availableCouponsAdapter = new AvailableCouponsAdapter(user, getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        availableCouponsRecyclerView.setLayoutManager(layoutManager);
        availableCouponsRecyclerView.setAdapter(availableCouponsAdapter);

        mViewModel.getUser().observe(getActivity(), data -> {
            user.setAvailableCoupons(data.getAvailableCoupons());
            if (data.getAvailableCoupons() == 0) {
                noAvailableCouponsTxt.setVisibility(View.VISIBLE);
                availableCouponsRecyclerView.setVisibility(View.GONE);
            } else {
                noAvailableCouponsTxt.setVisibility(View.GONE);
                availableCouponsRecyclerView.setVisibility(View.VISIBLE);
            }
            availableCouponsAdapter.notifyDataSetChanged();
        });

        loadData();

        SharedPreferences sharedPref = getContext().getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SHOW_REWARDS_BADGE, false);
        editor.apply();

        return view;
    }

    private void loadData() {
        redeemedCouponsAdapter = new RedeemedCouponsAdapter(couponList, getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);

        redeemedCouponsRecyclerView.setLayoutManager(layoutManager);
//        redeemedCouponsRecyclerView.setHasFixedSize(true);
        redeemedCouponsRecyclerView.setAdapter(redeemedCouponsAdapter);

        mViewModel.getMyCoupons().observe(getActivity(), data -> {
            couponList.clear();
            Collections.sort(data, new CouponComparator());

            couponList.addAll(data);
            if (data.size() == 0) {
                noRedeemedCouponsTxt.setVisibility(View.VISIBLE);
                redeemedCouponsRecyclerView.setVisibility(View.GONE);
            } else {
                noRedeemedCouponsTxt.setVisibility(View.GONE);
                redeemedCouponsRecyclerView.setVisibility(View.VISIBLE);
            }
            redeemedCouponsAdapter.notifyDataSetChanged();
        });
    }

    private void setupAvailableCouponsRecyclerView() {

    }

    @Override
    public void onclick(Coupon coupon) {
        CouponSelectFragment couponSelectFragment = new CouponSelectFragment();
        couponSelectFragment.show(getFragmentManager(), " CouponSelectFragment");
    }

    class CouponComparator implements Comparator<Coupon> {
        @Override
        public int compare(Coupon o1, Coupon o2) {
            return o2.getRedeemedDate().compareTo(o1.getRedeemedDate()); // Reverse order for descending
        }
    }
}
