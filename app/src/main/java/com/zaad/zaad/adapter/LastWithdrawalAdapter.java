package com.zaad.zaad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.model.Withdrawal;

import java.text.SimpleDateFormat;
import java.util.List;

public class LastWithdrawalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Withdrawal> withdrawalList;

    private Context context;

    public LastWithdrawalAdapter(List<Withdrawal> withdrawalList, Context context) {
        this.withdrawalList = withdrawalList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.last_withdrawal_item,
                        viewGroup, false);

        return new LastWithdrawalAdapter.LastWithdrawalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        LastWithdrawalAdapter.LastWithdrawalViewHolder lastWithdrawalViewHolder =
                (LastWithdrawalAdapter.LastWithdrawalViewHolder) viewHolder;

        Withdrawal withdrawal = withdrawalList.get(position);

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(withdrawal.getRequestedDate());

        lastWithdrawalViewHolder.withdrawalAmountTxt.setText(String.valueOf(withdrawal.getAmount()));
        lastWithdrawalViewHolder.withdrawalDate.setText(date);
        lastWithdrawalViewHolder.withdrawlStatus.setText(withdrawal.getStatus());
    }

    @Override
    public int getItemCount() {
        return withdrawalList.size();
    }

    class LastWithdrawalViewHolder extends RecyclerView.ViewHolder {
        TextView withdrawalAmountTxt, withdrawalDate, withdrawlStatus;

        LastWithdrawalViewHolder(View itemView) {
            super(itemView);
            withdrawalAmountTxt = itemView.findViewById(R.id.withdrawalAmount);
            withdrawalDate = itemView.findViewById(R.id.withdrawalDate);
            withdrawlStatus = itemView.findViewById(R.id.withdraw_status);
        }
    }
}
