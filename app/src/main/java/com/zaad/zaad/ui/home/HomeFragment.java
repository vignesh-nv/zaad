package com.zaad.zaad.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.zaad.zaad.BuildConfig;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.HomeAdBannerVideosAdapter;
import com.zaad.zaad.adapter.HomeAdapter;
import com.zaad.zaad.databinding.FragmentHomeBinding;
import com.zaad.zaad.listeners.AdCompleteListener;
import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.KhanzoPlayApp;
import com.zaad.zaad.model.User;
import com.zaad.zaad.model.Video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements AdCompleteListener {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private String RV_SCROLL_POSITION = "RV_SCROLL_POSITION";
    LinearLayoutManager layoutManager;
    Parcelable listState;

    private List<AdBanner> videoAdBanners = new ArrayList<>();
    HomeAdBannerVideosAdapter adBannerVideosAdapter;
    LinearLayoutManager adLayoutManager;
    ViewPager2 adViewPager;
    HomeViewModel homeViewModel;

    FirebaseFirestore firestore;

    private List<HomeItem> items = new ArrayList<>();

    HomeAdapter homeAdapter;
    FirebaseUser firebaseUser;

    User user;

    boolean running = false;

    boolean appUpdateShownAlready = false;

    List<AdBanner> ads;

    int counter = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ads = new ArrayList<>();

        getUserDetails();
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        recyclerView = binding.recyclerView;
//        adRecyclerView = binding.adSliderRecyclerview;

        layoutManager = new LinearLayoutManager(getContext());
        adLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        adBannerVideosAdapter = new HomeAdBannerVideosAdapter(videoAdBanners, getContext(), this);
        adViewPager = binding.adSliderVp;
        adViewPager.setAdapter(adBannerVideosAdapter);
//        adRecyclerView.setAdapter(adBannerVideosAdapter);
//        adRecyclerView.setHasFixedSize(true);
//        adRecyclerView.setLayoutManager(adLayoutManager);


        final int radius = getResources().getDimensionPixelSize(R.dimen.radius);
        final int dotsHeight = getResources().getDimensionPixelSize(R.dimen.dots_height);
        final int color = ContextCompat.getColor(getContext(), R.color.white);
//        adRecyclerView.addItemDecoration(new DotsIndicatorDecoration(radius, radius * 4, dotsHeight, color, color));
//        adRecyclerView.addItemDecoration(new CirclePagerIndicatorDecorator());
//        adRecyclerView.addItemDecoration(new LinePagerIndicationDecoration());

//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(adRecyclerView);

        homeAdapter = new HomeAdapter(items, ads, getContext());

        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(layoutManager);
//        homeViewModel.getCategories();


        homeViewModel.getLiveCategoryData().observe(getActivity(), categoryList -> {
            Log.i("", "onCreateView: " + categoryList);
        });

        readLatestAppVersionFromFirebase();
        return root;
    }

    private void getAdVideoBannerData() {
        homeViewModel.getVideoAdBanner(user).observe(getActivity(), data -> {
            videoAdBanners.clear();
            videoAdBanners.addAll(data);
            if (data.size() == 0) {
                adViewPager.setVisibility(View.GONE);
            }
            adBannerVideosAdapter.notifyDataSetChanged();
        });
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(RV_SCROLL_POSITION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
        setRetainInstance(true);
    }

    private void getHomeMenu() {
        homeViewModel.getHomeMenu().observe(getViewLifecycleOwner(), data -> {
            items.clear();
            if (running) {
                return;
            }
            running = true;
            final int[] count = {0};
            List<HomeItem> tempHomeItems = new ArrayList<>();

//            int counter = 0;
            for (HomeItem item : data) {
//                counter++;
//                if (counter % 2 == 0) {
//                    items.add(null);
//                }
                Log.i("HomeFragment Item", item.getTitle());
                if (item.getVideoCategory() != null && !item.getVideoCategory().equals(""))
                    firestore.collection(item.getCollection())
                            .whereEqualTo("language", user.getLanguage())
                            .whereEqualTo("state", user.getState())
                            .whereEqualTo("category", item.getVideoCategory())
                            .whereArrayContains("districts", user.getDistrict())
                            .limit(10)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<Video> videos = new ArrayList<>();
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    videos.add(snapshot.toObject(Video.class));
                                }
                                Collections.sort(videos);
                                count[0]++;
                                if (videos.size() != 0) {
                                    item.setVideos(videos);
                                    tempHomeItems.add(item);
                                }
                                if (count[0] == data.size()) {
                                    running = false;
                                    Collections.sort(tempHomeItems);
                                    notifyData(tempHomeItems);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Error", e.toString());
                                }
                            });
                else {
                    if (item.isLanguageFilter()) {
                        List<String> categoryFilter = item.getCategoryFilter();
                        if (categoryFilter == null) {
                            firestore.collection(item.getCollection())
                                    .whereEqualTo("language", user.getLanguage())
                                    .orderBy("uploadDate", Query.Direction.DESCENDING)
                                    .limit(10)
                                    .get()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("HomeFragment", e.toString());
                                        }
                                    })
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        List<Video> videos = new ArrayList<>();
                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            videos.add(snapshot.toObject(Video.class));
                                        }
                                        Collections.sort(videos);
                                        count[0]++;
                                        if (videos.size() != 0) {
                                            item.setVideos(videos);
                                            tempHomeItems.add(item);
                                        }
                                        if (count[0] == data.size()) {
                                            running = false;
                                            Collections.sort(tempHomeItems);
                                            notifyData(tempHomeItems);
                                        }
                                    });
                        } else {
                            firestore.collection(item.getCollection())
                                    .whereEqualTo("language", user.getLanguage())
                                    .whereNotIn("category", categoryFilter)
                                    .orderBy("uploadDate", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("HomeFragment", e.toString());
                                        }
                                    })
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        List<Video> videos = new ArrayList<>();
                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            videos.add(snapshot.toObject(Video.class));
                                        }
                                        Collections.sort(videos);

                                        count[0]++;
                                        if (videos.size() != 0) {
                                            item.setVideos(videos.subList(0, Math.min(10, videos.size())));
                                            tempHomeItems.add(item);
                                        }
                                        if (count[0] == data.size()) {
                                            running = false;
                                            Collections.sort(tempHomeItems);
                                            notifyData(tempHomeItems);
                                        }
                                    });
                        }
                    } else {
                        firestore.collection(item.getCollection())
                                .limit(10)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    List<Video> videos = new ArrayList<>();
                                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                        videos.add(snapshot.toObject(Video.class));
                                    }
                                    Collections.sort(videos);

                                    count[0]++;
                                    if (videos.size() != 0) {
                                        item.setVideos(videos);
                                        tempHomeItems.add(item);
                                    }

                                    if (count[0] == data.size()) {
                                        running = false;
                                        Collections.sort(tempHomeItems);
                                        notifyData(tempHomeItems);
                                    }
                                });
                    }
                }
            }
        });
    }
    private void notifyData(List<HomeItem> tempHomeItems) {
        int req = tempHomeItems.size() + tempHomeItems.size() / 2;

        List<HomeItem> newTempItems = new ArrayList<>(Collections.nCopies(req, new HomeItem()));

        for (int i = 2; i < req; i = i + 3) {
            newTempItems.set(i, null);
        }
        int counter = 0;
        for (int i = 0; i < newTempItems.size(); i++) {
            if (newTempItems.get(i) != null && counter < tempHomeItems.size()) {
                newTempItems.set(i, tempHomeItems.get(counter));
                counter++;
            }
        }

        items.clear();
        items.addAll(newTempItems);
        homeAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onComplete(int position) {
        int newPosition = (position + 1) % adBannerVideosAdapter.getItemCount();
//        adLayoutManager.smoothScrollToPosition(adRecyclerView, null, newPosition);
        adViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                adViewPager.setCurrentItem(newPosition, true);
            }
        }, 200);
    }

    private void getUserDetails() {
        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    getHomeMenu();
                    loadAds();
                    getAdVideoBannerData();
                });
    }

    private void readLatestAppVersionFromFirebase() {
        firestore.collection("appVersion")
                .document("appVersion")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    KhanzoPlayApp app = documentSnapshot.toObject(KhanzoPlayApp.class);
                    int versionCode = BuildConfig.VERSION_CODE;
                    if (app.getVersion() > versionCode) {
                        if (!appUpdateShownAlready) {
                            showAppUpdateDialog();
                        }

                    }
                });
    }
    private void showAppUpdateDialog() {
        appUpdateShownAlready = true;
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("App Update")
                .setMessage("New version of Khanzoplay is available. Upgrade your app for more features and better user experience")
                .setPositiveButton("Update", (dialogInterface, i) -> {
                    Uri uri = Uri.parse("https://khanzoplay.com/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                })
                .show();
    }

    private void loadAds() {
        firestore.collection("homeAds")
                .whereEqualTo("state", user.getState())
                .whereArrayContains("districts", user.getDistrict())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                        ads.add(snapshot.toObject(AdBanner.class));
                    }
                    homeAdapter.notifyDataSetChanged();
                });
    }
}
