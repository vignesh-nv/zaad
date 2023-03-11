package com.zaad.zaad.activity.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaad.zaad.R;
import com.zaad.zaad.VideoType;
import com.zaad.zaad.adapter.ChildVideosAdapter;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.viewmodel.ChildModeHomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChildModeHomeFragment extends Fragment {

    private ChildModeHomeViewModel mViewModel;

    private RecyclerView recyclerView;

    public static ChildModeHomeFragment newInstance() {
        return new ChildModeHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ChildModeHomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_child_mode_home, container, false);

        recyclerView = view.findViewById(R.id.child_mode_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        ChildVideosAdapter childVideosAdapter = new ChildVideosAdapter(generateVideos(), getContext());

        recyclerView.setAdapter(childVideosAdapter);
        recyclerView.setLayoutManager(layoutManager);

//        mViewModel.getChildVideos().observe(getViewLifecycleOwner(), data -> {
//            childVideosList.clear();
//            childVideosList.addAll(data);
//            childVideosAdapter.notifyDataSetChanged();
//        });
        return view;
    }

    private List<HomeItem> generateVideos() {
        List<HomeItem> homeItems = new ArrayList<>();

        HomeItem homeItem = new HomeItem();
        homeItem.setTitle("Cartoon Videos");

        HomeItem homeItem1 = new HomeItem();
        homeItem1.setTitle("Song Videos");

        HomeItem homeItem2 = new HomeItem();
        homeItem2.setTitle("Shorts");

        List<Video> cartoonVideos = new ArrayList<>();
        Video video = new Video();
        video.setVideoUrl("");
        video.setImageUrl("https://i.ytimg.com/vi/fcH8Na5KiAg/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLDhwnQoJM-1cY_TSOHojLP6MNucKA");

        cartoonVideos.add(video);

        homeItem.setVideos(cartoonVideos);
        homeItem1.setVideos(cartoonVideos);
        homeItem2.setVideos(cartoonVideos);

        homeItems.add(homeItem);
        homeItems.add(homeItem1);
        homeItems.add(homeItem2);

        return homeItems;
    }
}