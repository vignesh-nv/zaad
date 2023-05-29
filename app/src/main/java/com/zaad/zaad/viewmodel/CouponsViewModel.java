package com.zaad.zaad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.repository.FirestoreRepository;
import com.zaad.zaad.repository.UserRepository;

import java.util.List;

public class CouponsViewModel extends AndroidViewModel {

    FirestoreRepository repository;
    UserRepository userRepository;

    public CouponsViewModel(@NonNull Application application) {
        super(application);
        repository = new FirestoreRepository();
        userRepository = new UserRepository();
    }

    public MutableLiveData<List<Coupon>> getCoupons(final String availability) {
        return repository.getCoupons(availability);
    }

    public void redeemCoupon(Coupon coupon) {
        userRepository.redeemCoupon(coupon);
    }

    public MutableLiveData<List<Coupon>> getOnlineCouponsByCategory(final String availability, final String category) {
        return repository.getOnlineCouponsByCategory(availability, category);
    }

    public void decrementAvailableCoupons() {
        userRepository.incrementAvailableCoupons(-1);
    }
}
