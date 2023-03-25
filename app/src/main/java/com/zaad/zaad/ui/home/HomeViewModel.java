package com.zaad.zaad.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.Category;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.repository.FirestoreRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    private final MutableLiveData<String> mText;

    MutableLiveData<List<Category>> categories;

    MutableLiveData<List<AdBanner>> mutableVideoAdBanners;
    private FirestoreRepository firestoreRepository;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        firestoreRepository = new FirestoreRepository();
        categories = firestoreRepository.getCategoryListMutableLiveData();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Category>> getLiveCategoryData() {
        return categories;
    }

    public MutableLiveData<List<AdBanner>> getVideoAdBanner() {
        return firestoreRepository.getVideoAdBanners();
    }

    public MutableLiveData<List<HomeItem>> getHomeMenu() {
        return firestoreRepository.getHomeMenus();
    }
}