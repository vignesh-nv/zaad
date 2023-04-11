package com.zaad.zaad.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.OnlineShopFilterAdapter;
import com.zaad.zaad.listeners.OnDistrictFilterSelectedListener;

import java.util.List;

public class OnlineShopFilterBottomSheet extends BottomSheetDialogFragment {

    private OnDistrictFilterSelectedListener listeners;
    private List<String> names;

    public OnlineShopFilterBottomSheet(List<String> names, OnDistrictFilterSelectedListener listeners) {
        this.names = names;
        this.listeners = listeners;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offlin_shop_filter, container, false);

        ListView listView = view.findViewById(R.id.list_view);
        Button applyButton = view.findViewById(R.id.apply_button);

        OnlineShopFilterAdapter adapter = new OnlineShopFilterAdapter(getContext(), names);
        listView.setAdapter(adapter);

        applyButton.setOnClickListener(v -> {
            List<String> selectedNames = adapter.getSelectedNames();
            dismiss();

            listeners.onNamesSelected(selectedNames);
            // Do something with selected names here, e.g. pass them to the parent activity
        });

        return view;
    }
}

