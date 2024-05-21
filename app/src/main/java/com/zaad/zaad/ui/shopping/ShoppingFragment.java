package com.zaad.zaad.ui.shopping;

import static com.zaad.zaad.constants.AppConstant.SELECTED_DISTRICT_FILTER;
import static com.zaad.zaad.constants.AppConstant.SELECTED_STATE_FILTER;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ShoppingMenuAdapter;
import com.zaad.zaad.databinding.FragmentMusicBinding;
import com.zaad.zaad.model.AdBanner;
import com.zaad.zaad.model.ShoppingMenu;
import com.zaad.zaad.model.ShoppingMenuItem;
import com.zaad.zaad.model.State;
import com.zaad.zaad.model.User;
import com.zaad.zaad.utils.AppUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingFragment extends Fragment {

    private FragmentMusicBinding binding;

    private RecyclerView recyclerView;
    ShoppingViewModel shoppingViewModel;
    private FirebaseFirestore firestore;

    private List<ShoppingMenu> shoppingMenu = new ArrayList<>();
    private List<AdBanner> adBanners = new ArrayList<>();

    private boolean running;
    private Spinner spinnerState;
    private Spinner spinnerDistrict;

    private TextView filterText, districtNameText;

    Map<String, List<String>> stateDistrictMap = new HashMap<>();

    Button filterBtn;

    User user;

    String selectedDistrict, selectedState;
    ShoppingMenuAdapter shoppingMenuAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shoppingViewModel =
                new ViewModelProvider(this).get(ShoppingViewModel.class);

        firestore = FirebaseFirestore.getInstance();
        binding = FragmentMusicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        filterText = binding.filterText;

        recyclerView = binding.musicRecyclerView;
        districtNameText = binding.districtNameText;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        shoppingMenuAdapter = new ShoppingMenuAdapter(shoppingMenu, getContext());

        recyclerView.setAdapter(shoppingMenuAdapter);
        recyclerView.setLayoutManager(layoutManager);

        filterText.setOnClickListener(view -> {
            showStateDistrictDialog();
        });

        if (!loadSelectedStateAndDistrict()){
            getUserData();
        } else {
            getUserDataAndLoadAd();
        }
        return root;
    }

    private void loadData() {
        List<ShoppingMenuItem> onlineShops = new ArrayList<>();
        List<ShoppingMenuItem> offlineShops = new ArrayList<>();
        shoppingViewModel.getShoppingMenu().observe(getViewLifecycleOwner(), data -> {
            shoppingMenu.clear();
            onlineShops.clear();
            offlineShops.clear();
            for (ShoppingMenuItem shoppingMenu : data) {
                if (shoppingMenu.getAvailability().equals("ONLINE")) {
                    onlineShops.add(shoppingMenu);
                } else {
                    offlineShops.add(shoppingMenu);
                }
            }
            ShoppingMenu onlineItem = new ShoppingMenu();
            onlineItem.setShoppingMenuItemList(onlineShops);
            onlineItem.setAvailability("ONLINE");
            onlineItem.setTitle("Online Shops");

            ShoppingMenu offlineItem = new ShoppingMenu();
            offlineItem.setShoppingMenuItemList(offlineShops);
            offlineItem.setAvailability("OFFLINE");
            offlineItem.setTitle("Offline Shops");

            if (offlineShops.size() != 0)
                shoppingMenu.add(offlineItem);
            if (adBanners != null && adBanners.size() > 0) {
                ShoppingMenu shoppingMenu1 = new ShoppingMenu();
                shoppingMenu1.setImageUrl(adBanners.get(0).getImageUrl());
                shoppingMenu1.setLink(adBanners.get(0).getLink());
                shoppingMenu.add(shoppingMenu1);
            }

            if (onlineShops.size() != 0)
                shoppingMenu.add(onlineItem);

            shoppingMenuAdapter.notifyDataSetChanged();
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showStateDistrictDialog() {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.state_district_dialog, null);
        spinnerState = dialogView.findViewById(R.id.spinnerState);
        spinnerDistrict = dialogView.findViewById(R.id.spinnerDistrict);
        filterBtn = dialogView.findViewById(R.id.filter_btn);

        // Create and set up the AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView)
                .setTitle("Select State and District")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedState = spinnerState.getSelectedItem().toString();
                        String selectedDistrict = spinnerDistrict.getSelectedItem().toString();
                        // Handle the selected state and district
                    }
                })
                .setNegativeButton("Cancel", null);

        // Populate the spinners with data (you can use your data source here)
        ArrayList<String> stateList = new ArrayList<>();
        ArrayList<String> districtList = new ArrayList<>();

        String jsonString = AppUtils.getJsonFromAssets(getActivity(), "districts.json");
        Gson gson = new Gson();
        Type listStateType = new TypeToken<List<State>>() {
        }.getType();
        List<State> states = gson.fromJson(jsonString, listStateType);

        for (State state : states) {
            stateDistrictMap.put(state.getName(), state.getDistricts());
            stateList.add(state.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateList);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, districtList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapter);

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtList.clear();
                List<String> strings = stateDistrictMap.get(stateList.get(i));
                districtList.addAll(strings);
                districtAdapter.notifyDataSetChanged();
                selectedState = stateList.get(i);
                selectedDistrict = strings.get(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDistrict = districtList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.setCancelable(false);

        filterBtn.setOnClickListener(view -> {
            saveSelectedFilter();
            districtNameText.setText(selectedDistrict);
            dialog.cancel();
        });
    }

    private void loadBannerAds(final String state, final String district) {
        firestore.collection("ads")
                .whereEqualTo("adType", "SHOP_AD")
                .whereArrayContains("districts", district)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                        adBanners.add(snapshot.toObject(AdBanner.class));
                    }
                    loadData();
                });
    }

    private void getUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String email = firebaseUser.getEmail();

        firestore.collection("user").document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    selectedState = user.getState();
                    selectedDistrict = user.getDistrict();
                    districtNameText.setText(selectedDistrict);
                    saveSelectedFilter();
                    loadBannerAds(selectedState, selectedDistrict);
                });
    }

    private void getUserDataAndLoadAd() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String email = firebaseUser.getEmail();

        firestore.collection("user").document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    loadBannerAds(user.getState(), user.getDistrict());
                });
    }

    private boolean loadSelectedStateAndDistrict() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        selectedState = sharedPref.getString(SELECTED_STATE_FILTER, null);
        selectedDistrict = sharedPref.getString(SELECTED_DISTRICT_FILTER, null);
        districtNameText.setText(selectedDistrict);
        return selectedDistrict != null;
    }

    private void saveSelectedFilter() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SELECTED_STATE_FILTER, selectedState);
        editor.putString(SELECTED_DISTRICT_FILTER, selectedDistrict);
        editor.apply();
    }
}