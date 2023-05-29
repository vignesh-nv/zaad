package com.zaad.zaad.ui.music;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.repository.FirestoreRepository;

import java.util.List;

public class MusicViewModel extends ViewModel {

    FirestoreRepository firestoreRepository;

    public MusicViewModel() {
        firestoreRepository = new FirestoreRepository();
    }

    public MutableLiveData<List<HomeItem>> getYoutubeVideosMenu() {
        return firestoreRepository.getMusicVideosMenu();
    }
}