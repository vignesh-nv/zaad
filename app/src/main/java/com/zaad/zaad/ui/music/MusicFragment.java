package com.zaad.zaad.ui.music;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ChildVideosAdapter;
import com.zaad.zaad.databinding.FragmentMusicBinding;
import com.zaad.zaad.model.HomeItem;
import com.zaad.zaad.model.Video;
import com.zaad.zaad.viewmodel.ChildModeHomeViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicFragment extends Fragment {

    private FragmentMusicBinding binding;

    private RecyclerView recyclerView;
    MusicViewModel musicViewModel;
    private FirebaseFirestore firestore;

    private List<HomeItem> musicMenu = new ArrayList<>();

    private boolean running;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        musicViewModel =
                new ViewModelProvider(this).get(MusicViewModel.class);

        firestore = FirebaseFirestore.getInstance();
        binding = FragmentMusicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.musicRecyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        ChildVideosAdapter childVideosAdapter = new ChildVideosAdapter(musicMenu, getContext());

        recyclerView.setAdapter(childVideosAdapter);
        recyclerView.setLayoutManager(layoutManager);

        musicViewModel.getYoutubeVideosMenu().observe(getViewLifecycleOwner(), data -> {
            musicMenu.clear();
            if (running) {
                return;
            }
            running = true;
            for (HomeItem item : data) {
                Log.i("MusicFragment", item.toString());
                firestore.collection("music").whereEqualTo("category", item.getCategory())
                        .limit(10)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<Video> videos = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                videos.add(snapshot.toObject(Video.class));
                            }
                            Log.i("Category", item.getCategory() + " Size: " + videos.size());
                            if (videos.size()!=0) {
                                Collections.sort(videos);
                                item.setVideos(videos);
                                musicMenu.add(item);
                            }
                            childVideosAdapter.notifyDataSetChanged();
                        });
//                mViewModel.getVideosByCategory(item.getCategory()).observe(getViewLifecycleOwner(), videos -> {
//                    item.setVideos(videos);
//                    Log.i(item.getCategory(), videos.toString());
//                    kidsMenu.add(item);
//                });
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}