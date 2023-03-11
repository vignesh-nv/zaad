package com.zaad.zaad.ui.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ChildVideosAdapter;
import com.zaad.zaad.databinding.FragmentMusicBinding;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.viewmodel.ChildModeHomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment {

    private FragmentMusicBinding binding;

    private ChildModeHomeViewModel mViewModel;

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MusicViewModel musicViewModel =
                new ViewModelProvider(this).get(MusicViewModel.class);

        binding = FragmentMusicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.musicRecyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        ChildVideosAdapter childVideosAdapter = new ChildVideosAdapter(generateVideos(), getContext());

        recyclerView.setAdapter(childVideosAdapter);
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<HomeItem> generateVideos() {
        List<HomeItem> homeItems = new ArrayList<>();

        HomeItem homeItem = new HomeItem();
        homeItem.setTitle("Latest Songs");

        HomeItem homeItem1 = new HomeItem();
        homeItem1.setTitle("Tamil Songs");

        HomeItem homeItem2 = new HomeItem();
        homeItem2.setTitle("Telugu Songs");

        List<Video> cartoonVideos = new ArrayList<>();
        Video video = new Video();
        video.setVideoUrl("");
        video.setImageUrl("https://i.ytimg.com/vi/HfMTwkVQohM/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLD3nmJRxcdRybjXk9KURg-epULVdA");

        Video video1 = new Video();
        video1.setVideoUrl("");
        video1.setImageUrl("https://i.ytimg.com/vi/5BQQM4uvRkw/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLAn1LHxREk816KdBcSjVbxpkITWbA");

        cartoonVideos.add(video);
        cartoonVideos.add(video1);

        List<Video> trendingVideos = new ArrayList<>();
        Video video2 = new Video();
        video2.setImageUrl("https://i.ytimg.com/vi/YxWlaYCA8MU/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLALnJ7IheE9Y6XgTRGJQ-j9l9CpaA");
        video2.setVideoUrl("");

        Video video3 = new Video();
        video3.setImageUrl("https://i.ytimg.com/vi/V5cV30yFXLA/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLCfxRhM850Q3XEnvCzkFQu50viFyA");
        trendingVideos.add(video2);
        trendingVideos.add(video3);


        List<Video> teluguSongs = new ArrayList<>();
        Video video4 = new Video();
        video4.setImageUrl("https://i.ytimg.com/vi/8c49--Q-TaM/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLDCmW7SEru8Z1BWXrFlXK33lZdYEw");
        video4.setVideoUrl("");

        Video video5 = new Video();
        video5.setImageUrl("https://i.ytimg.com/vi/XWqPjlDWtP0/hq720.jpg?sqp=-oaymwEcCNAFEJQDSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLCAr1CYbxGOLlwuaVROMhbcQW6hcg");

        teluguSongs.add(video4);
        teluguSongs.add(video5);

        homeItem.setVideos(trendingVideos);
        homeItem1.setVideos(cartoonVideos);
        homeItem2.setVideos(teluguSongs);

        homeItems.add(homeItem);
        homeItems.add(homeItem1);
        homeItems.add(homeItem2);

        return homeItems;
    }
}