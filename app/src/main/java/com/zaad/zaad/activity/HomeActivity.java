package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.GetData;
import com.zaad.zaad.R;
import com.zaad.zaad.databinding.ActivityHomeBinding;
import com.zaad.zaad.repository.FirestoreRepository;
import com.zaad.zaad.utils.FindData;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = binding.toolbar;

        FindData findData = new FindData(getApplicationContext(), (GetData) (linkList, message, isData) -> {
            if (isData) {
                if (linkList.size() != 0) {
                    Log.i("Download Urls", linkList.toString());
                }
            } else {
            }
        });
        findData.data("https://www.instagram.com/reel/CoAiTpdtmvJ/");
        bottomNavigationView = findViewById(R.id.bottomNavView);

        setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_daily_task, R.id.navigation_wallet,
                R.id.navigation_rewards, R.id.navigation_music)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);

//        binding.navView.getMenu().findItem(R.id.logout_button).getActionView().setOnClickListener(view -> {
//            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent()
//        });

        SwitchMaterial materialSwitch = (SwitchMaterial) binding.navView.getMenu().findItem(R.id.kid_mode_switch).getActionView();
        materialSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            saveChildModeInSharedPreference();
            Intent intent = new Intent(HomeActivity.this, ChildModeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void saveChildModeInSharedPreference() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CHILD_MODE, true);
        editor.apply();
    }
}