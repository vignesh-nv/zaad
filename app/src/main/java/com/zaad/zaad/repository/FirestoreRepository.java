package com.zaad.zaad.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.Category;
import com.zaad.zaad.model.Coupon;
import com.zaad.zaad.model.DailyTaskVideo;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.Shop;
import com.zaad.zaad.model.Video;

import org.checkerframework.checker.index.qual.UpperBoundUnknown;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirestoreRepository {

    FirebaseFirestore mFirestore;
    MutableLiveData<List<Category>> categoryListMutableLiveData;
    MutableLiveData<Category> categoryMutableLiveData;

    MutableLiveData<List<Video>> videoListMutableLiveData;

    MutableLiveData<List<Video>> childVideosListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<DailyTaskVideo>> dailyTaskVideosListMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<Coupon>> couponsListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Coupon>> onlineCouponsByCategoryListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Shop>> shopListMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<Video>> youtubeVideosMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<Video>> youtubeAllVideosMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<Video>> youtubeVideosByCollectionMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<DailyTaskVideo>> dailyTaskShortsMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<AdBanner>> videoAdBannersMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<HomeItem>> kisVideosMenuMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<HomeItem>> musicMenuMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<HomeItem>> homeMenuMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<Video>> kisVideosForMenuMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Video>> kisVideosByCategoryMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Video> videoMutableLiveData;

    public FirestoreRepository() {
        this.categoryListMutableLiveData = new MutableLiveData<>();
        mFirestore = FirebaseFirestore.getInstance();
        categoryMutableLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<Category>> getCategoryListMutableLiveData() {
        Log.i("TAG", "getCategoryListMutableLiveData: ");
        mFirestore.collection("video").addSnapshotListener((value, error) -> {
            List<Category> categoryList = new ArrayList<>();
            for (QueryDocumentSnapshot doc : value) {
                if (doc != null) {
                    categoryList.add(doc.toObject(Category.class));
                    Log.i("FirestoreRepository", "Data -> " + doc.getData());
                }
            }
            categoryListMutableLiveData.postValue(categoryList);
        });
        return categoryListMutableLiveData;
    }

    public MutableLiveData<List<Video>> getVideoListByLimit() {
        Log.i("TAG", "getVideoListMutableLiveData: ");
        mFirestore.collection("video").addSnapshotListener((value, error) -> {
            List<Video> videoList = new ArrayList<>();
            for (QueryDocumentSnapshot doc : value) {
                if (doc != null) {
                    videoList.add(doc.toObject(Video.class));
                    Log.i("FirestoreRepository", "Data -> " + doc.getData());
                }
            }
            videoListMutableLiveData.postValue(videoList);
        });
        return videoListMutableLiveData;
    }

    public MutableLiveData<List<Shop>> getShopList(final String availability) {

        mFirestore.collection("shops").whereEqualTo("availability", availability)
                .addSnapshotListener((value, error) -> {
                    List<Shop> videoList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc != null) {
                            videoList.add(doc.toObject(Shop.class));
                            Log.i("FirestoreRepository", "Data -> " + doc.getData());
                        }
                    }
                    shopListMutableLiveData.postValue(videoList);
                });
        return shopListMutableLiveData;
    }

    public MutableLiveData<List<Video>> getChildVideos() {
        mFirestore.collection("childVideos").addSnapshotListener((value, error) -> {
            List<Video> videoList = new ArrayList<>();
            for (QueryDocumentSnapshot doc : value) {
                if (doc != null) {
                    videoList.add(doc.toObject(Video.class));
                    Log.i("FirestoreRepository", "Data -> " + doc.getData());
                }
            }
            childVideosListMutableLiveData.postValue(videoList);
        });
        return childVideosListMutableLiveData;
    }

    public MutableLiveData<List<DailyTaskVideo>> getDailyTasks() {
        mFirestore.collection("dailyTasks").whereGreaterThan("expiryDate", new Date()).addSnapshotListener((value, error) -> {
            Date currentDate = new Date();
            List<DailyTaskVideo> dailyTaskVideos = new ArrayList<>();
            for (QueryDocumentSnapshot doc : value) {
                if (doc != null) {
                    DailyTaskVideo dailyTaskVideo = doc.toObject(DailyTaskVideo.class);
                    Date videoDate = dailyTaskVideo.getStartDate();
                    if (videoDate != null && currentDate.after(videoDate) && dailyTaskVideo.getCategory().equals("Video")) {
                        dailyTaskVideos.add(doc.toObject(DailyTaskVideo.class));
                    }
                }
            }
            dailyTaskVideosListMutableLiveData.postValue(dailyTaskVideos);
        });
        return dailyTaskVideosListMutableLiveData;
    }

    public MutableLiveData<List<Coupon>> getCoupons() {
        mFirestore.collection("coupons").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Coupon> coupons = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                if (queryDocumentSnapshot != null) {
                    coupons.add(queryDocumentSnapshot.toObject(Coupon.class));
                }
            }
            couponsListMutableLiveData.postValue(coupons);
        });
        return couponsListMutableLiveData;
    }

    public MutableLiveData<List<Coupon>> getOnlineCouponsByCategory(final String category) {
        mFirestore.collection("coupons").whereEqualTo("category", category).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Coupon> coupons = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                if (queryDocumentSnapshot != null) {
                    coupons.add(queryDocumentSnapshot.toObject(Coupon.class));
                }
            }
            onlineCouponsByCategoryListMutableLiveData.postValue(coupons);
        });
        return onlineCouponsByCategoryListMutableLiveData;
    }

    public MutableLiveData<List<Video>> getYoutubeVideosByCategory(final String category) {
        mFirestore.collection("youtube").whereEqualTo("category", category).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Video> videos = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                if (queryDocumentSnapshot != null) {
                    videos.add(queryDocumentSnapshot.toObject(Video.class));
                }
            }
            youtubeVideosMutableLiveData.postValue(videos);
        });
        return youtubeVideosMutableLiveData;
    }

    public MutableLiveData<List<Video>> getYoutubeVideos() {
        mFirestore.collection("youtube").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Video> videos = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                if (queryDocumentSnapshot != null) {
                    videos.add(queryDocumentSnapshot.toObject(Video.class));
                }
            }
            youtubeAllVideosMutableLiveData.postValue(videos);
        });
        return youtubeAllVideosMutableLiveData;
    }

    public MutableLiveData<List<Video>> getYoutubeVideosByCollectionAndCategory(final String collections, final String category) {
        mFirestore.collection(collections).whereEqualTo("category", category).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Video> videos = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                if (queryDocumentSnapshot != null) {
                    videos.add(queryDocumentSnapshot.toObject(Video.class));
                }
            }
            youtubeVideosByCollectionMutableLiveData.postValue(videos);
        });
        return youtubeVideosByCollectionMutableLiveData;
    }

    public MutableLiveData<List<DailyTaskVideo>> getDailyTaskShorts() {
        mFirestore.collection("dailyTasks").whereEqualTo("category", "Shorts").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DailyTaskVideo> videos = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                if (queryDocumentSnapshot != null) {
                    videos.add(queryDocumentSnapshot.toObject(DailyTaskVideo.class));
                }
            }
            dailyTaskShortsMutableLiveData.postValue(videos);
        });
        return dailyTaskShortsMutableLiveData;
    }

    public MutableLiveData<List<AdBanner>> getVideoAdBanners() {
        mFirestore.collection("videoAdBanner").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<AdBanner> videos = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                if (queryDocumentSnapshot != null) {
                    videos.add(queryDocumentSnapshot.toObject(AdBanner.class));
                }
            }
            videoAdBannersMutableLiveData.postValue(videos);
        });
        return videoAdBannersMutableLiveData;
    }

    public MutableLiveData<List<HomeItem>> getKidVideosMenu() {
        mFirestore.collection("kidsVideosMenu").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<HomeItem> items = new ArrayList<>();
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                items.add(snapshot.toObject(HomeItem.class));
            }
            kisVideosMenuMutableLiveData.postValue(items);
        });
        return kisVideosMenuMutableLiveData;
    }

    public MutableLiveData<List<Video>> getChildVideosForHomePage() {
        mFirestore.collection("kidsVideos")
                .orderBy("category")
                .limit(2)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Video> videos = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        videos.add(snapshot.toObject(Video.class));
                    }
                    kisVideosForMenuMutableLiveData.postValue(videos);
                });
        return kisVideosForMenuMutableLiveData;
    }

    public MutableLiveData<List<Video>> getChildVideosByCategory(final String category) {
        mFirestore.collection("kidsVideos")
                .whereEqualTo("category", category)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Video> videos = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        videos.add(snapshot.toObject(Video.class));
                    }
                    kisVideosByCategoryMutableLiveData.postValue(videos);
                });
        return kisVideosByCategoryMutableLiveData;
    }

    public MutableLiveData<List<HomeItem>> getMusicVideosMenu() {
        mFirestore.collection("musicVideosMenu").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<HomeItem> items = new ArrayList<>();
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                items.add(snapshot.toObject(HomeItem.class));
            }
            musicMenuMutableLiveData.postValue(items);
        });
        return musicMenuMutableLiveData;
    }

    public MutableLiveData<List<HomeItem>> getHomeMenus() {
        mFirestore.collection("homeMenu").orderBy("order", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<HomeItem> items = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                    items.add(snapshot.toObject(HomeItem.class));
                }
                homeMenuMutableLiveData.postValue(items);
            }
        });
        return homeMenuMutableLiveData;
    }
}
