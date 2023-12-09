package com.zaad.zaad.ui.music;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.ShoppingMenu;
import com.zaad.zaad.model.ShoppingMenuItem;
import com.zaad.zaad.repository.FirestoreRepository;

import java.util.List;

public class ShoppingViewModel extends ViewModel {

    FirestoreRepository firestoreRepository;

    public ShoppingViewModel() {
        firestoreRepository = new FirestoreRepository();
    }

    public MutableLiveData<List<HomeItem>> getYoutubeVideosMenu() {
        return firestoreRepository.getMusicVideosMenu();
    }

    public MutableLiveData<List<ShoppingMenuItem>> getShoppingMenu() {
        return firestoreRepository.getShoppingMenu();
    }
}