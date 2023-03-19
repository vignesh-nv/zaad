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
import java.util.List;

public class HomeFragment extends Fragment  implements AdCompleteListener {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private String RV_SCROLL_POSITION = "RV_SCROLL_POSITION";
    LinearLayoutManager layoutManager;
    Parcelable listState;

    private List<AdBanner> videoAdBanners = new ArrayList<>();
    HomeAdBannerVideosAdapter adBannerVideosAdapter;
    LinearLayoutManager adLayoutManager;
    ViewPager2 adViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

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

        HomeAdapter parentItemAdapter = new HomeAdapter(generateList(), getContext());

        recyclerView.setAdapter(parentItemAdapter);
        recyclerView.setLayoutManager(layoutManager);
//        homeViewModel.getCategories();

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

    private List<AdBanner> generateAdBanners() {
        List<AdBanner> adBanners = new ArrayList<>();
        AdBanner adBanner = new AdBanner();
        adBanner.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/zaad-cb167.appspot.com/o/adBanner%2FHappy%20diwali%20from%20BAIRACORP%20%23diwali%20%23festival%20%23happy.mp4?alt=media&token=8a588596-cd1d-4f83-8035-94a8dcc60506");
        adBanner.setImageUrl("https://i.ytimg.com/vi/dYykpGxJMMs/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLD2MISYPSLmrFYAMJPxHsJlaOTbMw");

        AdBanner adBanner1 = new AdBanner();
        adBanner1.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/zaad-cb167.appspot.com/o/adBanner%2Fdownload.mp4?alt=media&token=35f45f5c-cbd9-47b1-8e62-26b883850540");
        adBanner1.setImageUrl("https://i.ytimg.com/vi/nuGjrQ64J0U/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLBDVvn9Y13JxD4NhDSXYn-xaSDvuA");

        AdBanner adBanner2 = new AdBanner();
        adBanner2.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/zaad-cb167.appspot.com/o/adBanner%2Fvideoplayback.mp4?alt=media&token=43bd8b66-a0d2-43d3-bc91-d50c44886a9a");
        adBanner2.setImageUrl("https://i.ytimg.com/vi/nuGjrQ64J0U/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLBDVvn9Y13JxD4NhDSXYn-xaSDvuA");

        adBanners.add(adBanner);
        adBanners.add(adBanner1);
        adBanners.add(adBanner2);
        adBanners.add(adBanner);

        return adBanners;
    }

    private List<HomeItem> generateList() {
        List<HomeItem> homeItems = new ArrayList<>();

        HomeItem trendingMenu = new HomeItem();
        trendingMenu.setTitle("Trending Videos");
        trendingMenu.setCategory(VideoType.YOUTUBE_VIDEO.toString());

        HomeItem youtubeVideosMenu = new HomeItem();
        youtubeVideosMenu.setTitle("Youtube Videos");
        youtubeVideosMenu.setCategory(VideoType.YOUTUBE_VIDEO.toString());

        HomeItem facebookVideosMenu = new HomeItem();
        facebookVideosMenu.setTitle("Facebook Videos");
        facebookVideosMenu.setCategory(VideoType.FACEBOOK_VIDEOS.toString());

        HomeItem youtubeShortsMenu = new HomeItem();
        youtubeShortsMenu.setTitle("Shorts");
        youtubeShortsMenu.setCategory(VideoType.YOUTUBE_SHORTS.toString());

        HomeItem insta = new HomeItem();
        insta.setTitle("Instagram Reels");
        insta.setCategory(VideoType.INSTAGRAM_REEL.toString());

        HomeItem imageAD = new HomeItem();
        imageAD.setTitle("Ad");
        imageAD.setCategory(VideoType.IMAGE_AD.toString());

        HomeItem stores = new HomeItem();
        stores.setTitle("Online Store");
        stores.setCategory("ONLINE_STORE");

        HomeItem offlineStores = new HomeItem();
        offlineStores.setTitle("Offline Stores");
        offlineStores.setCategory("OFFLINE_STORE");

        Video video = new Video();
        video.setTitle("Video");
        video.setImageUrl("https://i.ytimg.com/vi/KbDQX1VU77A/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLDB8fYDOiWnSi_ClWu89XlpyMVf-w");
        video.setVideoUrl("KbDQX1VU77A");

        Video video1 = new Video();
        video1.setTitle("Video");
        video1.setImageUrl("https://i.ytimg.com/vi/cZImVVQ8WNI/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLCMl2tzFVWWACJ3WS3WMK5yftGg4A");
        video1.setVideoUrl("cZImVVQ8WNI");

        Video video2 = new Video();
        video2.setTitle("Video");
        video2.setImageUrl("https://i.ytimg.com/vi/JtPbk7WvHAQ/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLDUUW5Uc4Aim_RZuXftq3a425AR1Q");
        video2.setVideoUrl("JtPbk7WvHAQ");

        Video adImage = new Video();
        adImage.setImageUrl("https://firebasestorage.googleapis.com/v0/b/zaad-cb167.appspot.com/o/adBanner%2FSnapinsta.app_1080_296301010_841288773503739_1395188615003701265_n.jpg?alt=media&token=8c453e1a-4720-4eed-9a74-c3c2ce45134e");

        Video adImage2 = new Video();
        adImage2.setImageUrl("https://firebasestorage.googleapis.com/v0/b/zaad-cb167.appspot.com/o/adBanner%2FSnapinsta.app_1080_298361927_457540369347458_5389873869384240667_n.jpg?alt=media&token=96a8a2c2-e5a7-4e6a-952a-4b6d32524f9f");

        List<Video> adImages = new ArrayList<>();
        adImages.add(adImage);
        adImages.add(adImage2);

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

        trendingMenu.setVideos(videoList);
        youtubeVideosMenu.setVideos(videoList);
        facebookVideosMenu.setVideos(videoList);
        youtubeShortsMenu.setVideos(shortsList);
        insta.setVideos(instaReels);
        stores.setVideos(videoList);
        offlineStores.setVideos(videoList);
        imageAD.setVideos(adImages);

        homeItems.add(trendingMenu);
        homeItems.add(youtubeVideosMenu);
        homeItems.add(youtubeShortsMenu);
        homeItems.add(facebookVideosMenu);
        homeItems.add(insta);
        homeItems.add(imageAD);
        homeItems.add(stores);
        homeItems.add(offlineStores);
        return homeItems;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onComplete(int position) {
        int newPosition = (position + 1) % adBannerVideosAdapter.getItemCount();
        Log.i("HomeFragment", "Position" + newPosition);
//        adLayoutManager.smoothScrollToPosition(adRecyclerView, null, newPosition);
        adViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                adViewPager.setCurrentItem(newPosition, true);
            }
        }, 200);
    }
}
