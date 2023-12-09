package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.SELECTED_DISTRICT_FILTER;
import static com.zaad.zaad.constants.AppConstant.SELECTED_STATE_FILTER;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ShoppingAdapter;
import com.zaad.zaad.dialog.OnlineShopFilterBottomSheet;
import com.zaad.zaad.listeners.OnContactClickListener;
import com.zaad.zaad.listeners.OnDistrictFilterSelectedListener;
import com.zaad.zaad.model.Shop;
import com.zaad.zaad.model.State;
import com.zaad.zaad.model.User;
import com.zaad.zaad.utils.AppUtils;
import com.zaad.zaad.viewmodel.ShopViewModel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShoppingTimeActivity extends AppCompatActivity implements OnContactClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 132;
    RecyclerView recyclerView;
    List<Shop> shopList = new ArrayList<>();

    private String availability, category;

    TextView districtNameText;
    ShopViewModel shopViewModel;
    ShoppingAdapter shoppingAdapter;

    List<String> districts;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    User user;
    View filterLayout;

    HorizontalScrollView chipsLayout, offlineChipsLayout;

    List<String> selectedDistricts;

    private String selectedDistrict, selectedState;

    ImageView bannerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_time);
        availability = getIntent().getStringExtra("SHOP_TYPE");
        category = getIntent().getStringExtra("CATEGORY");
        recyclerView = findViewById(R.id.shopping_time_recycler_view);
        districtNameText = findViewById(R.id.district_name_text);
        filterLayout = findViewById(R.id.filter_layout);

        loadSelectedStateAndDistrict();
        bannerImageView = findViewById(R.id.banner_image);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        shopViewModel =
                new ViewModelProvider(this).get(ShopViewModel.class);

        shoppingAdapter = new ShoppingAdapter(shopList, this, availability, this);

        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    readStatesAndDistrictData();
                    if (availability.equals("OFFLINE")) {
                        getSupportActionBar().setTitle("Offline Stores");
                        loadOfflineShopsByCategory(category);
                    } else {
                        getSupportActionBar().setTitle("Online Stores");
                        loadOnlineShopsByCategory(category);
                        filterLayout.setVisibility(View.GONE);
                    }
                });
//        filterText.setOnClickListener(view -> {
////            OnlineShopFilterBottomSheet bottomSheet = new OnlineShopFilterBottomSheet(districts,
////                    ShoppingTimeActivity.this);
////            bottomSheet.show(getSupportFragmentManager(), "MyBottomSheetDialog");
//            Intent intent = new Intent(this, ShoppingFilterActivity.class);
//            intent.putExtra("USER", user);
//            intent.putExtra("FILTER_DISTRICTS", (Serializable) selectedDistricts);
//            startActivity(intent);
//        });

        bannerImageView.setOnClickListener(view -> {

        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(shoppingAdapter);

//        if (selectedDistricts != null && selectedDistricts.size() != 0) {
//            loadShopsByDistricts();
//        }
    }

    public void loadShopsByDistricts() {
        shopViewModel.getOfflineShopByDistrict(selectedDistricts).observe(this, data -> {
            shopList.clear();
            shopList.addAll(data);
            shoppingAdapter.notifyDataSetChanged();
        });
    }

    private void loadOnlineShopsByCategory(final String category) {
        shopViewModel.getShopByCategory("ONLINE", category).observe(this, data -> {
            shopList.clear();
            shopList.addAll(data);
            shoppingAdapter.notifyDataSetChanged();
        });
    }

    private void loadOfflineShopsByCategory(final String category) {
        if (selectedState != null && selectedDistrict != null) {
            shopViewModel.getOfflineShopByDistrictAndCategory(selectedState, selectedDistrict, category).observe(this, data -> {
                shopList.clear();
                shopList.addAll(data);
                shoppingAdapter.notifyDataSetChanged();
            });
        } else {
            shopViewModel.getOfflineShopByCategory(user.getState(), category).observe(this, data -> {
                shopList.clear();
                shopList.addAll(data);
                shoppingAdapter.notifyDataSetChanged();
            });
        }
    }

    private void readStatesAndDistrictData() {
        String jsonString = AppUtils.getJsonFromAssets(this, "districts.json");
        Gson gson = new Gson();
        Type listStateType = new TypeToken<List<State>>() {
        }.getType();

        List<State> states = gson.fromJson(jsonString, listStateType);

        for (State state : states) {
            if (state.getName().equals(user.getState())) {
                districts = state.getDistricts();
            }
        }
    }

    @Override
    public void onContactClicked(String number) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void loadSelectedStateAndDistrict() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        selectedState = sharedPref.getString(SELECTED_STATE_FILTER, null);
        selectedDistrict = sharedPref.getString(SELECTED_DISTRICT_FILTER, null);
        districtNameText.setText(selectedDistrict);
    }
}
