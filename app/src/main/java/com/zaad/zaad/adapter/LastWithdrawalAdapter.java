package com.zaad.zaad.adapter;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        lastWithdrawalViewHolder.upiIdTxt.setText(withdrawal.getUpiId());
        lastWithdrawalViewHolder.accountNumberTxt.setText(withdrawal.getAccountNumber());

        lastWithdrawalViewHolder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastWithdrawalViewHolder.hiddenView.getVisibility() == View.VISIBLE) {
                    // The transition of the hiddenView is carried out by the TransitionManager class.
                    // Here we use an object of the AutoTransition Class to create a default transition
                    TransitionManager.beginDelayedTransition((ViewGroup) lastWithdrawalViewHolder.cardView, new AutoTransition());
                    lastWithdrawalViewHolder.hiddenView.setVisibility(View.GONE);
                    lastWithdrawalViewHolder.expand.setImageResource(R.drawable.ic_arrow_expand);
                } else {
                    TransitionManager.beginDelayedTransition((ViewGroup) lastWithdrawalViewHolder.cardView, new AutoTransition());
                    lastWithdrawalViewHolder.hiddenView.setVisibility(View.VISIBLE);
                    lastWithdrawalViewHolder.expand.setImageResource(R.drawable.ic_arrow_less);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return withdrawalList.size();
    }

    class LastWithdrawalViewHolder extends RecyclerView.ViewHolder {
        TextView withdrawalAmountTxt, withdrawalDate, withdrawlStatus, upiIdTxt, accountNumberTxt;

        ImageView expand;
        View hiddenView;
        View cardView;

        LastWithdrawalViewHolder(View itemView) {
            super(itemView);
            withdrawalAmountTxt = itemView.findViewById(R.id.withdrawalAmount);
            withdrawalDate = itemView.findViewById(R.id.withdrawalDate);
            withdrawlStatus = itemView.findViewById(R.id.withdraw_status);
            expand = itemView.findViewById(R.id.expand_details);
            hiddenView = itemView.findViewById(R.id.hidden_view);
            cardView = itemView.findViewById(R.id.cardview);
            upiIdTxt = itemView.findViewById(R.id.upi_id_txt);
            accountNumberTxt = itemView.findViewById(R.id.account_number_txt);
        }
    }
}
