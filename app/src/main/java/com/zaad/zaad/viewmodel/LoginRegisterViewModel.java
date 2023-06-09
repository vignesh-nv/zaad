package com.zaad.zaad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zaad.zaad.model.ReferralData;
import com.zaad.zaad.model.User;
import com.zaad.zaad.repository.UserRepository;

public class LoginRegisterViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    public User user;

    public LoginRegisterViewModel(@NonNull final Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public void saveUser(final User user) {
        userRepository.saveUser(user);
    }

    public MutableLiveData<Boolean> validateReferralCode(final String referralCode) {
        return userRepository.isValidReferralCode(referralCode);
    }

    public void saveReferralData(final ReferralData referralData) {
        userRepository.saveReferralData(referralData);
    }
}

