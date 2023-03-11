package com.zaad.zaad.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.zaad.zaad.VideoType;
import com.zaad.zaad.adapter.HomeAdBannerVideosAdapter;
import com.zaad.zaad.adapter.HomeAdapter;
import com.zaad.zaad.databinding.FragmentHomeBinding;
import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.Video;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView, adRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recyclerView;
        adRecyclerView = binding.adSliderRecyclerview;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        HomeAdBannerVideosAdapter adBannerVideosAdapter = new HomeAdBannerVideosAdapter(generateAdBanners(), getContext());
        adRecyclerView.setAdapter(adBannerVideosAdapter);
        adRecyclerView.setHasFixedSize(true);
        adRecyclerView.setLayoutManager(horizontalLayoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(adRecyclerView);

        HomeAdapter parentItemAdapter = new HomeAdapter(generateList(), getContext());

        recyclerView.setAdapter(parentItemAdapter);
        recyclerView.setLayoutManager(layoutManager);
//        homeViewModel.getCategories();

        homeViewModel.getLiveCategoryData().observe(getActivity(), categoryList -> {
            Log.i("", "onCreateView: " + categoryList);
        });

        return root;
    }

    private List<AdBanner> generateAdBanners() {
        List<AdBanner> adBanners = new ArrayList<>();
        AdBanner adBanner = new AdBanner();
        adBanner.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/zaad-cb167.appspot.com/o/videoplayback%20(1).webm?alt=media&token=f50251c4-dba8-4778-a29b-888f8c0122c8");
        adBanner.setImageUrl("https://i.ytimg.com/vi/dYykpGxJMMs/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLD2MISYPSLmrFYAMJPxHsJlaOTbMw");

        AdBanner adBanner1 = new AdBanner();
        adBanner1.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/zaad-cb167.appspot.com/o/videoplayback.webm?alt=media&token=70ae0921-d3a4-48af-ab86-7da6191c8a49");
        adBanner1.setImageUrl("https://i.ytimg.com/vi/nuGjrQ64J0U/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLBDVvn9Y13JxD4NhDSXYn-xaSDvuA");

        adBanners.add(adBanner);
        adBanners.add(adBanner1);
        adBanners.add(adBanner);
        return adBanners;
    }
    private List<HomeItem> generateList() {
        List<HomeItem> homeItems = new ArrayList<>();

        HomeItem homeItem = new HomeItem();
        homeItem.setTitle("Youtube Videos");
        homeItem.setCategory(VideoType.YOUTUBE_VIDEO.toString());

        HomeItem homeItem1 = new HomeItem();
        homeItem1.setTitle("Facebook Videos");
        homeItem1.setCategory(VideoType.YOUTUBE_SHORTS.toString());

        HomeItem homeItem2 = new HomeItem();
        homeItem2.setTitle("Shorts");
        homeItem2.setCategory(VideoType.YOUTUBE_SHORTS.toString());

        HomeItem insta = new HomeItem();
        insta.setTitle("Instagram Reels");
        insta.setCategory(VideoType.INSTAGRAM_REEL.toString());

        HomeItem stores = new HomeItem();
        stores.setTitle("Online Store");
        stores.setCategory("ONLINE_STORE");

        HomeItem offlineStores = new HomeItem();
        offlineStores.setTitle("Offline Stores");
        offlineStores.setCategory("OFFLINE_STORE");

        Video video = new Video();
        video.setTitle("Video");
        video.setImageUrl("https://i.ytimg.com/vi/F0CIXNJplhY/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLCzq65sft_XLnJfqyWIwnfRLweNJA");

        Video video1 = new Video();
        video1.setTitle("Video");
        video1.setImageUrl("https://i.ytimg.com/vi/SFdGyt0V00M/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLCbSJZ7wGrNOhf5Kwu06lH-lO8Fgg");

        Video video2 = new Video();
        video2.setTitle("Video");
        video2.setImageUrl("https://i.ytimg.com/vi/5KZlvadN7Z4/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLC8JrJKESjep_E2NtTW3pHZbkpNXQ");

        List<Video> videoList = new ArrayList<>();
        videoList.add(video);
        videoList.add(video1);
        videoList.add(video2);
        videoList.add(video2);
        videoList.add(video2);

        List<Video> shortsList = new ArrayList<>();
        Video video3 = new Video();
        video3.setImageUrl("https://i.ytimg.com/vi/8tnspkH83p4/hq720_2.jpg?sqp=-oaymwEdCJUDENAFSFXyq4qpAw8IARUAAIhCcAHAAQbQAQE=&rs=AOn4CLCXOOJ66tMjqkXjhr2vVkOjQrRdEQ");

        Video video4 = new Video();
        video4.setImageUrl("https://i.ytimg.com/vi/AXGPLWZeKLc/hq720_2.jpg?sqp=-oaymwEdCJUDENAFSFXyq4qpAw8IARUAAIhCcAHAAQbQAQE=&rs=AOn4CLD3hP6k7MsvQ2sGGNSvckCZTW576A");

        Video video5 = new Video();
        video5.setImageUrl("https://i.ytimg.com/vi/bCv4n6IMGBw/hq720_2.jpg?sqp=-oaymwEdCJUDENAFSFXyq4qpAw8IARUAAIhCcAHAAQbQAQE=&rs=AOn4CLCoLqtISPn3cOFkATAvHVyNzmo_lQ");

        Video video6 = new Video();
        video6.setImageUrl("https://i.ytimg.com/vi/8tnspkH83p4/hq720_2.jpg?sqp=-oaymwEdCJUDENAFSFXyq4qpAw8IARUAAIhCcAHAAQbQAQE=&rs=AOn4CLCXOOJ66tMjqkXjhr2vVkOjQrRdEQ");

        List<Video> instaReels = new ArrayList<>();
        Video instaVideo = new Video();
        instaVideo.setImageUrl("https://i.ytimg.com/vi/m3ZpP6XUVUM/hq720_2.jpg?sqp=-oaymwEdCJUDENAFSFTyq4qpAw8IARUAAIhCcAHAAQbQAQE=&rs=AOn4CLBf1Rfrbvp8jvfkyaebyvVYhHoz1A");

        Video instaVideo1 = new Video();
        instaVideo1.setImageUrl("https://i.ytimg.com/vi/L8mxe0f9Ewg/hq720_2.jpg?sqp=-oaymwEdCJUDENAFSFTyq4qpAw8IARUAAIhCcAHAAQbQAQE=&rs=AOn4CLDoeZzygYpCcFu9Q4y65rqcAKgb5Q");

        instaReels.add(instaVideo);
        instaReels.add(instaVideo1);

        shortsList.add(video3);
        shortsList.add(video4);
        shortsList.add(video5);
        shortsList.add(video6);

        homeItem.setVideos(videoList);
        homeItem1.setVideos(videoList);
        homeItem2.setVideos(shortsList);
        insta.setVideos(instaReels);
        stores.setVideos(videoList);
        offlineStores.setVideos(videoList);

        homeItems.add(homeItem);
        homeItems.add(homeItem1);
        homeItems.add(homeItem2);
        homeItems.add(homeItem);
        homeItems.add(insta);
        homeItems.add(stores);
        homeItems.add(offlineStores);
        return homeItems;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
