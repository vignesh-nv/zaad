package com.zaad.zaad.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.zaad.zaad.R;
import com.zaad.zaad.ui.wallet.WalletViewModel;

public class WithdrawFragment extends BottomSheetDialogFragment {

    public WithdrawFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText amountTxt = view.findViewById(R.id.amount);
        Button withdrawBtn = view.findViewById(R.id.withdrawBtn);

        WalletViewModel walletViewModel = new ViewModelProvider(getActivity()).get(WalletViewModel.class);

        withdrawBtn.setOnClickListener(view1 -> {
            int amount = Integer.parseInt(amountTxt.getText().toString());
            if (amount < 500) {
                Toast.makeText(getContext(), "Amount is lesser than 500", Toast.LENGTH_SHORT).show();
                return;
            }
            walletViewModel.setWithdrawAmount(amount);
            dismiss();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        return view;
    }
}