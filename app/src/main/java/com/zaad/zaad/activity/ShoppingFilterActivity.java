package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.RedeemedCouponsAdapter;
import com.zaad.zaad.adapter.ShoppingFilterAdapter;
import com.zaad.zaad.model.State;
import com.zaad.zaad.model.User;
import com.zaad.zaad.utils.AppUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShoppingFilterActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button applyBtn;

    List<String> districts = new ArrayList<>();
    List<String> selectedDistricts = new ArrayList<>();

    User user;
    ShoppingFilterAdapter shoppingFilterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_filter);
        recyclerView = findViewById(R.id.filter_items);
        applyBtn = findViewById(R.id.apply_btn);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("District Filter");
        }

        user = (User) getIntent().getSerializableExtra("USER");
        selectedDistricts = (List<String>) getIntent().getSerializableExtra("FILTER_DISTRICTS");
        applyBtn.setOnClickListener(v -> {
            selectedDistricts = shoppingFilterAdapter.getAllSelectedDistricts();
            Intent intent = new Intent(ShoppingFilterActivity.this, ShoppingTimeActivity.class);
            intent.putExtra("SHOP_TYPE", "OFFLINE_STORE");
            intent.putExtra("FILTER_DISTRICTS", (Serializable) selectedDistricts);
            startActivity(intent);
            finish();

        });
        readStatesAndDistrictData();
    }

    private void readStatesAndDistrictData() {
        String jsonString = AppUtils.getJsonFromAssets(this, "districts.json");
        Gson gson = new Gson();
        Type listStateType = new TypeToken<List<State>>() {}.getType();

        List<State> states = gson.fromJson(jsonString, listStateType);

        for (State state : states) {
            if (state.getName().equals(user.getState())) {
                districts.addAll(state.getDistricts());
                shoppingFilterAdapter = new ShoppingFilterAdapter(districts, selectedDistricts);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(shoppingFilterAdapter);
                break;
            }
        }
    }
}