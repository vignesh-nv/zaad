package com.zaad.zaad.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.zaad.zaad.R;
import com.zaad.zaad.ui.wallet.WalletViewModel;

import org.w3c.dom.Text;

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
        View descriptionView = view.findViewById(R.id.withdrawal_description_view);
        TextView withdrawalAmountTxt = view.findViewById(R.id.withdrawal_amount_txt);
        TextView serviceChargeTxt = view.findViewById(R.id.service_charge_txt);
        TextView totalAmountTxt = view.findViewById(R.id.total_amount_txt);

        WalletViewModel walletViewModel = new ViewModelProvider(getActivity()).get(WalletViewModel.class);

        amountTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() == null || charSequence.toString().equals("")) {
                    withdrawalAmountTxt.setText("");
                    serviceChargeTxt.setText("");
                    totalAmountTxt.setText("");
                    return;
                }
                int amount = Integer.parseInt(charSequence.toString());
                if (amount < 500) {
                    withdrawalAmountTxt.setText("");
                    serviceChargeTxt.setText("");
                    totalAmountTxt.setText("");
                    return;
                }
                withdrawalAmountTxt.setText(String.valueOf(charSequence));
                int serviceCharge = (2 * amount / 100);
                serviceChargeTxt.setText(String.valueOf(serviceCharge));
                int total = amount - serviceCharge;
                totalAmountTxt.setText(String.valueOf(total));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        amountTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });
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