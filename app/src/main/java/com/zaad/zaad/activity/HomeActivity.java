package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.SHOW_REWARDS_BADGE;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.GetData;
import com.zaad.zaad.R;
import com.zaad.zaad.databinding.ActivityHomeBinding;
import com.zaad.zaad.model.User;
import com.zaad.zaad.repository.FirestoreRepository;
import com.zaad.zaad.utils.FindData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    FirebaseUser firebaseUser;

    FirebaseFirestore firestore;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = binding.toolbar;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

//        MobileAds.initialize(this, initializationStatus -> {
//        });

//        FindData findData = new FindData(getApplicationContext(), (GetData) (linkList, message, isData) -> {
//            if (isData) {
//                if (linkList.size() != 0) {
//                    Log.i("Download Urls", linkList.toString());
//                }
//            } else {
//            }
//        });
//        findData.data("https://www.instagram.com/reel/CoAiTpdtmvJ/");
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

        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        boolean showBadge = sharedPref.getBoolean(SHOW_REWARDS_BADGE, false);

        if (showBadge) {
            BadgeDrawable rewardsBadge = bottomNavigationView.getOrCreateBadge(R.id.navigation_rewards);
            rewardsBadge.setVisible(true);
        }

        isUserSubscribed();
        setBadge();
//        SwitchMaterial materialSwitch = (SwitchMaterial) binding.navView.getMenu().findItem(R.id.kid_mode_switch).getActionView();
//        materialSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
//            saveChildModeInSharedPreference();
//            Intent intent = new Intent(HomeActivity.this, ChildModeActivity.class);
//            startActivity(intent);
//            finish();
//        });
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

    private void isUserSubscribed() {
        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (!user.isPaymentCompleted()) {
                        showSubscribePopup(user);
                    }
                });
    }

    private void showSubscribePopup(final User user) {
        long diff = new Date().getTime() - user.getJoinedDate().getTime();
        long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
        boolean cancellable = false;
        if (hours <= 72) {
            new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setTitle("Khanzoplay Subscription")
                    .setMessage("Subscribe to Khanzoplay to Enjoy Premium benefits!!")
                    .setCancelable(cancellable)
                    .setPositiveButton("Subscribe", (dialogInterface, i) -> {
                        Intent intent = new Intent(this, PostPaymentActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Skip", ((dialogInterface, i) -> {

                    }))
                    .show();
        } else {
            new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setTitle("Khanzoplay Subscription")
                    .setMessage("Subscribe to Khanzoplay to Enjoy Premium benefits!!")
                    .setCancelable(cancellable)
                    .setPositiveButton("Subscribe", (dialogInterface, i) -> {
                        Intent intent = new Intent(this, PostPaymentActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    private void setBadge() {
        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    if (user.isNotificationAvailable()) {
                        BadgeDrawable badgeDrawable = BadgeDrawable.create(this);
                        BadgeUtils.attachBadgeDrawable(badgeDrawable, toolbar, R.id.notification_icon);
                    }
                });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification_icon:
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                updateNotificationAsViewed();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateNotificationAsViewed() {

        if (!user.isNotificationAvailable()){
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("notificationAvailable", false);

        firestore.collection("user").document(firebaseUser.getEmail())
                .update(map)
                .addOnSuccessListener(unused -> {
                });
    }
}