package com.zaad.zaad.ui.wallet;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zaad.zaad.model.Withdrawal;
import com.zaad.zaad.repository.UserRepository;

import java.util.List;

public class WalletViewModel extends AndroidViewModel {

    UserRepository userRepository;
    public MutableLiveData<Integer> withdrawAmount;


    public WalletViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
        withdrawAmount = new MutableLiveData<>();
    }

    public MutableLiveData<List<Withdrawal>> getWithdrawalList() {
        return userRepository.getWithdrawalList();
    }

    public LiveData<Integer> getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(Integer amount) {
        withdrawAmount.postValue(amount);
    }

    public void makeWithdrawTransaction(final Withdrawal withdrawal) {
        userRepository.makeWithdrawTransaction(withdrawal);
    }

    public void reduceAmountFromUserAccount(final int amount) {
        userRepository.reduceUserAmount(amount);
    }
}