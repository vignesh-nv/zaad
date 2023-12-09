package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.SELECTED_DISTRICT_FILTER;
import static com.zaad.zaad.constants.AppConstant.SELECTED_STATE_FILTER;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.adapter.ShoppingItemAdapter;
import com.zaad.zaad.model.ShoppingMenuItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingMenuActivity extends AppCompatActivity {

    RecyclerView menuRecyclerView;

    String availability;

    FirebaseFirestore firestore;

    List<ShoppingMenuItem> itemList;

    ShoppingItemAdapter adapter;

    TextView districtNameText;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_menu);

        itemList = new ArrayList<>();
        menuRecyclerView = findViewById(R.id.menu_recyclerView);
        districtNameText = findViewById(R.id.district_name_text);
        imageView = findViewById(R.id.ic_location_icon);

        availability = getIntent().getStringExtra("SHOP_TYPE");
        firestore = FirebaseFirestore.getInstance();

        if (availability.equals("OFFLINE")) {
            getSupportActionBar().setTitle("Offline Shop");
        } else {
            districtNameText.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Online shop");
            imageView.setVisibility(View.GONE);
        }
        adapter = new ShoppingItemAdapter(itemList, this, availability);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        menuRecyclerView.setLayoutManager(layoutManager);
        menuRecyclerView.setHasFixedSize(true);
        menuRecyclerView.setAdapter(adapter);
        loadDistrictDetails();
        firestore.collection("shopMenu")
                .whereEqualTo("availability", availability)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                        itemList.add(snapshot.toObject(ShoppingMenuItem.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void loadDistrictDetails() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        districtNameText.setText(sharedPref.getString(SELECTED_DISTRICT_FILTER, ""));
    }
}
