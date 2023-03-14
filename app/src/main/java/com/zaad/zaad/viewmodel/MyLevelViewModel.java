package com.zaad.zaad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zaad.zaad.model.User;
import com.zaad.zaad.repository.UserRepository;

import java.util.List;

public class MyLevelViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public MyLevelViewModel(@NonNull final Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public void saveUser(final User user) {
        userRepository.saveUser(user);
    }

    public MutableLiveData<User> getUser() {
        return userRepository.getUser();
    }

    public MutableLiveData<List<User>> getMyReferrals(final String referralCode) {
        return userRepository.getMyReferrals(referralCode);
    }
}

