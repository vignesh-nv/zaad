package com.zaad.zaad.ui.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.VideoType;
import com.zaad.zaad.adapter.HomeAdBannerVideosAdapter;
import com.zaad.zaad.adapter.HomeAdapter;
import com.zaad.zaad.databinding.FragmentHomeBinding;
import com.zaad.zaad.listeners.AdCompleteListener;
import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.utils.CirclePagerIndicatorDecorator;
import com.zaad.zaad.utils.DotsIndicatorDecoration;
import com.zaad.zaad.utils.LinePagerIndicationDecoration;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
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
    boolean running = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        firestore = FirebaseFirestore.getInstance();

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

        homeAdapter = new HomeAdapter(items, getContext());

        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(layoutManager);
//        homeViewModel.getCategories();

        getHomeMenu();
        homeViewModel.getLiveCategoryData().observe(getActivity(), categoryList -> {
            Log.i("", "onCreateView: " + categoryList);
        });

        homeViewModel.getVideoAdBanner().observe(getActivity(), data -> {
            videoAdBanners.clear();
            videoAdBanners.addAll(data);
            adBannerVideosAdapter.notifyDataSetChanged();
        });
        return root;
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
            for (HomeItem item : data) {
                Log.i("HomeFragment Item", item.getTitle());
                firestore.collection(item.getCollection())
                        .limit(10)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<Video> videos = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                                videos.add(snapshot.toObject(Video.class));
                            }
                            count[0]++;
                            item.setVideos(videos);
                            tempHomeItems.add(item);
                            if (count[0] == data.size()) {
                                running = false;
                                Collections.sort(tempHomeItems);
                                items.addAll(tempHomeItems);
                                Log.i("HomeFragment menu", String.valueOf(items.size()));
                                homeAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
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
}
