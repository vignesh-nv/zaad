package com.zaad.zaad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zaad.zaad.model.User;
import com.zaad.zaad.repository.UserRepository;

public class MyAccountViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public MyAccountViewModel(@NonNull final Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public void saveUser(final User user) {
        userRepository.saveUser(user);
    }

    public MutableLiveData<User> getUser() {
        return userRepository.getUser();
    }
}

