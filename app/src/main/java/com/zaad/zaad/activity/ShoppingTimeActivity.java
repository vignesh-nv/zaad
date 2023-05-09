package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
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

public class ShoppingTimeActivity extends AppCompatActivity implements OnDistrictFilterSelectedListener {

    RecyclerView recyclerView;
    List<Shop> shopList = new ArrayList<>();

    private String category, shopType;

    TextView filterText;
    ShopViewModel shopViewModel;
    ShoppingAdapter shoppingAdapter;

    List<String> districts;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    User user;
    View filterLayout;

    HorizontalScrollView chipsLayout;

    ChipGroup onlineShopChipGroup;

    List<String> selectedDistricts;

    Chip groceryChip, mobilesChip, fashionChip, electronicsChip, homeChip, personalCareChip, appliancesChip;
    Chip toysBabyChip, furnitureChip, flightsChip, nutritionChip, bikesCarChip, medicinesChip, sportsChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_time);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    readStatesAndDistrictData();
                });
        category = getIntent().getStringExtra("SHOP_TYPE");

        selectedDistricts = (List<String>) getIntent().getSerializableExtra("FILTER_DISTRICTS");

        filterText = findViewById(R.id.filter_text);
        filterLayout = findViewById(R.id.filter_layout);
        chipsLayout = findViewById(R.id.chips_layout);

        filterText.setOnClickListener(view -> {
//            OnlineShopFilterBottomSheet bottomSheet = new OnlineShopFilterBottomSheet(districts,
//                    ShoppingTimeActivity.this);
//            bottomSheet.show(getSupportFragmentManager(), "MyBottomSheetDialog");
            Intent intent = new Intent(this, ShoppingFilterActivity.class);
            intent.putExtra("USER", user);
            intent.putExtra("FILTER_DISTRICTS", (Serializable) selectedDistricts);
            startActivity(intent);
        });
        if (category.equals("OFFLINE_STORE")) {
            shopType = "OFFLINE";
            getSupportActionBar().setTitle("Offline Stores");
            chipsLayout.setVisibility(View.GONE);
        } else {
            shopType = "ONLINE";
            getSupportActionBar().setTitle("Online Stores");
            filterLayout.setVisibility(View.GONE);
        }
        shopViewModel =
                new ViewModelProvider(this).get(ShopViewModel.class);

        recyclerView = findViewById(R.id.shopping_time_recycler_view);

        shoppingAdapter = new ShoppingAdapter(shopList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);

        shopViewModel.getShopList(shopType).observe(this, data -> {
            shopList.clear();
            shopList.addAll(data);
            shoppingAdapter.notifyDataSetChanged();

        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(shoppingAdapter);
        setupUI();
        setupOnlineShopFiltering();
        if (selectedDistricts!=null && selectedDistricts.size()!=0) {
            loadShopsByDistricts();
        }
    }

    private void setupUI() {
        groceryChip = findViewById(R.id.grocery_chip);
        mobilesChip = findViewById(R.id.mobile_chip);
        fashionChip = findViewById(R.id.fashion_chip);
        electronicsChip = findViewById(R.id.electronics_chip);
        homeChip = findViewById(R.id.home_chip);
        personalCareChip = findViewById(R.id.personal_care_chip);
        appliancesChip = findViewById(R.id.appliances_chip);
        toysBabyChip = findViewById(R.id.toys_chip);
        furnitureChip = findViewById(R.id.furniture_chip);
        flightsChip = findViewById(R.id.flights_hotels_chip);
        nutritionChip = findViewById(R.id.nutrition_chip);
        bikesCarChip = findViewById(R.id.bikes_cars_chip);
        medicinesChip = findViewById(R.id.medicines_chip);
        sportsChip = findViewById(R.id.sports_chip);
        onlineShopChipGroup = findViewById(R.id.online_shop_chipgroup);
    }

    private void setupOnlineShopFiltering() {

        onlineShopChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.size() == 0) {
                loadAllOnlineShops();
                return;
            }
            int checkChipId = onlineShopChipGroup.getCheckedChipId();
            if (groceryChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("grocery");
            } else if (mobilesChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("mobiles");
            } else if (fashionChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("fashion");
            } else if (electronicsChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("electronics");
            } else if (homeChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("home");
            } else if (personalCareChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("personalcare");
            } else if (appliancesChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("appliances");
            } else if (toysBabyChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("toys");
            } else if (furnitureChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("furniture");
            } else if (flightsChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("flights");
            } else if (nutritionChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("nutrition");
            } else if (bikesCarChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("bikes");
            } else if (medicinesChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("medicines");
            } else if (sportsChip.getId() == checkChipId) {
                loadOnlineShopsByCategory("sports");
            }
        });
    }

    @Override
    public void onNamesSelected(List<String> selectedDistricts) {
        shopViewModel.getOfflineShopByDistrict(selectedDistricts).observe(this, data -> {
            shopList.clear();
            shopList.addAll(data);
            shoppingAdapter.notifyDataSetChanged();
        });
    }

    public void loadShopsByDistricts() {
        shopViewModel.getOfflineShopByDistrict(selectedDistricts).observe(this, data -> {
            shopList.clear();
            shopList.addAll(data);
            shoppingAdapter.notifyDataSetChanged();
        });
    }

    private void loadOnlineShopsByCategory(final String category) {
        shopViewModel.getOnlineShopByCategory(category).observe(this, data -> {
            shopList.clear();
            shopList.addAll(data);
            shoppingAdapter.notifyDataSetChanged();
        });
    }
    private void loadAllOnlineShops() {
        shopViewModel.getShopList(shopType).observe(this, data -> {
            shopList.clear();
            shopList.addAll(data);
            shoppingAdapter.notifyDataSetChanged();
        });
    }

    private void readStatesAndDistrictData() {
        String jsonString = AppUtils.getJsonFromAssets(this, "districts.json");
        Gson gson = new Gson();
        Type listStateType = new TypeToken<List<State>>() {}.getType();

        List<State> states = gson.fromJson(jsonString, listStateType);

        for (State state : states) {
            if (state.getName().equals(user.getState())) {
                districts = state.getDistricts();
            }
        }
    }
}
