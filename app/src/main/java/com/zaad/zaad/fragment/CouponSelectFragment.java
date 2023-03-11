package com.zaad.zaad.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zaad.zaad.R;
import com.zaad.zaad.activity.CouponsActivity;

public class CouponSelectFragment extends BottomSheetDialogFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon_select, container, false);
        View offlineCard = view.findViewById(R.id.offline_coupons);
        View onlineCard = view.findViewById(R.id.online_coupons);

        offlineCard.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CouponsActivity.class);
            startActivity(intent);
        });

        onlineCard.setOnClickListener(view12 -> {

            Intent intent = new Intent(getActivity(), CouponsActivity.class);
            startActivity(intent);
        });
        return view;
    }
}