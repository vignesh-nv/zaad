package com.zaad.zaad.ui.rewards;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.model.User;
import com.zaad.zaad.repository.UserRepository;

import java.util.List;

public class RewardsViewModel extends AndroidViewModel {

    UserRepository userRepository;

    public RewardsViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public MutableLiveData<List<Coupon>> getMyCoupons() {
        return userRepository.getMyCoupons();
    }

    public MutableLiveData<User> getUser() {
        return userRepository.getUser();
    }
}