package com.zaad.zaad.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zaad.zaad.model.Shop;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.repository.FirestoreRepository;

import java.util.List;

public class YoutubeVideosViewModel extends AndroidViewModel {

    FirestoreRepository firestoreRepository;

    public YoutubeVideosViewModel(@NonNull Application application) {
        super(application);
        firestoreRepository = new FirestoreRepository();
    }

    public MutableLiveData<List<Video>> getYoutubeVideosByCategory(final String category) {
        return firestoreRepository.getYoutubeVideosByCategory(category);
    }

    public MutableLiveData<List<Video>> getYoutubeVideosByCollectionAndCategory(final String collection, final String category) {
        return firestoreRepository.getYoutubeVideosByCollectionAndCategory(collection, category);
    }

    public MutableLiveData<List<Video>> getAllYoutubeVideos() {
        return firestoreRepository.getYoutubeVideos();
    }
}
