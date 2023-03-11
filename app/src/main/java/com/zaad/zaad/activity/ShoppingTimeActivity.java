package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ShoppingAdapter;
import com.zaad.zaad.model.Shop;
import com.zaad.zaad.viewmodel.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingTimeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Shop> shopList = new ArrayList<>();

    private String category, shopType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_time);

        category = getIntent().getStringExtra("SHOP_TYPE");

        if (category.equals("OFFLINE_STORE")) {
            shopType = "OFFLINE";
        } else {
            shopType = "ONLINE";
        }
        ShopViewModel shopViewModel =
                new ViewModelProvider(this).get(ShopViewModel.class);

        recyclerView = findViewById(R.id.shopping_time_recycler_view);

        ShoppingAdapter shoppingAdapter = new ShoppingAdapter(shopList, this);
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
    }

    private List<Shop> generateList() {
        List<Shop> shopList = new ArrayList<>();
        shopList.add(new Shop());
        shopList.add(new Shop());
        shopList.add(new Shop());
        return shopList;
    }
}