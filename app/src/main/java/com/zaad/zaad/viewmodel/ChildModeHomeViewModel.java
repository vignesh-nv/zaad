package com.zaad.zaad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zaad.zaad.model.Video;
import com.zaad.zaad.repository.FirestoreRepository;

import java.util.List;

public class ChildModeHomeViewModel extends AndroidViewModel {

    FirestoreRepository firestoreRepository;

    public ChildModeHomeViewModel(@NonNull Application application) {
        super(application);
        firestoreRepository = new FirestoreRepository();
    }

    public MutableLiveData<List<Video>> getChildVideos() {
        return firestoreRepository.getChildVideos();
    }
}