package com.zaad.zaad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zaad.zaad.model.Shop;
import com.zaad.zaad.repository.FirestoreRepository;

import java.util.List;

public class ShopViewModel extends AndroidViewModel {

    FirestoreRepository firestoreRepository;

    public ShopViewModel(@NonNull Application application) {
        super(application);
        firestoreRepository = new FirestoreRepository();
    }

    public MutableLiveData<List<Shop>> getShopList(String availability) {
        if (availability.equals("OFFLINE")) {
            return firestoreRepository.getOfflineShops();
        }
        return firestoreRepository.getOnlineShops();
    }

    public MutableLiveData<List<Shop>> getOfflineShopByDistrict(List<String> districts) {
        return firestoreRepository.getOfflineShopByDistricts(districts);
    }

    public MutableLiveData<List<Shop>> getOnlineShopByCategory(final String category) {
        return firestoreRepository.getOnlineShopListByCategory(category);
    }
}
