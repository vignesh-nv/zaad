package com.zaad.zaad.ui.wallet;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.LastWithdrawalAdapter;
import com.zaad.zaad.databinding.FragmentWalletBinding;
import com.zaad.zaad.fragment.WithdrawFragment;
import com.zaad.zaad.model.User;
import com.zaad.zaad.model.Withdrawal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WalletFragment extends Fragment {

    private WalletViewModel mViewModel;
    FragmentWalletBinding binding;

    RecyclerView recyclerView;
    Button withdrawBtn;

    TextView userBalanceTxt;

    List<Withdrawal> withdrawalList = new ArrayList<>();
    private WalletViewModel walletViewModel;
    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        walletViewModel =
                new ViewModelProvider(requireActivity()).get(WalletViewModel.class);

        binding = FragmentWalletBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.lastWithdrawalRv;
        withdrawBtn = binding.withdrawButton;
        userBalanceTxt = binding.userBalance;

        walletViewModel.userRepository.getUser().observe(getActivity(), data -> {
            user = data;
            userBalanceTxt.setText(String.valueOf(user.getAmount()));
        });

        withdrawBtn.setOnClickListener(view -> {
            WithdrawFragment withdrawFragment = new WithdrawFragment();
            withdrawFragment.show(getFragmentManager(), " WithdrawFragment");
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        LastWithdrawalAdapter withdrawalAdapter = new LastWithdrawalAdapter(withdrawalList, getContext());
        recyclerView.setAdapter(withdrawalAdapter);
        recyclerView.setLayoutManager(layoutManager);

        walletViewModel.getWithdrawalList().observe(getActivity(), data -> {
            withdrawalList.clear();
            withdrawalList.addAll(data);
            withdrawalAdapter.notifyDataSetChanged();
        });

        walletViewModel.withdrawAmount.removeObservers(this);

        walletViewModel.getWithdrawAmount().observe(getViewLifecycleOwner(), amount -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                makeWithdrawTransaction(amount);
            }
        });
    }

    private void makeWithdrawTransaction(int amount) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setRequestedDate(new Date());
        walletViewModel.makeWithdrawTransaction(withdrawal);
        walletViewModel.reduceAmountFromUserAccount(user.getAmount() - amount);
    }
}
