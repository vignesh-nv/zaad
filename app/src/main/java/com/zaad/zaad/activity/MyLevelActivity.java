package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.MyLevelViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyLevelActivity extends AppCompatActivity {

    TextView referralCodeTxt;
    MyLevelViewModel myLevelViewModel;
    private User user;

    Button shareBtn;

    TextView referral1NameTxt, referral1EmailTxt, referral1JoinedDataTxt, referral1LevelTxt, myLevelTxt;
    TextView referral2NameTxt, referral2EmailTxt, referral2JoinedDataTxt, referral2LevelTxt, active1, active2;
    View referral1Layout, referral2Layout;

    private List<User> myReferrals = new ArrayList<>();

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_level);

        firestore = FirebaseFirestore.getInstance();
        shareBtn = findViewById(R.id.shareBtn);
        referralCodeTxt = findViewById(R.id.referralCodeTxt);
        myLevelTxt = findViewById(R.id.my_level_text);
        myLevelViewModel = new ViewModelProvider(this).get(MyLevelViewModel.class);

        setupUI();

        myLevelViewModel.getUser().observe(this, data -> {
            user = data;
            referralCodeTxt.setText(user.getReferralCode());
            myLevelTxt.setText(user.getLevel());
            getMyReferrals();
        });

        shareBtn.setOnClickListener(view -> {

        });
    }

    private void setupUI() {
        referral1NameTxt = findViewById(R.id.referral_user_name);
        referral1EmailTxt = findViewById(R.id.email);
        referral1JoinedDataTxt = findViewById(R.id.joined_date);
        referral1LevelTxt = findViewById(R.id.level);
        referral1Layout = findViewById(R.id.referral1Layout);
        active1 = findViewById(R.id.referral_1_active);

        referral2Layout = findViewById(R.id.referral2Layout);
        referral2LevelTxt = findViewById(R.id.level_2);
        referral2JoinedDataTxt = findViewById(R.id.joined_date_2);
        referral2EmailTxt = findViewById(R.id.email_2);
        referral2NameTxt = findViewById(R.id.referral_user_name_2);
        active2 = findViewById(R.id.referral_2_active);
    }

    private void getMyReferrals() {
        myLevelViewModel.getMyReferrals(user.getReferralCode()).observe(this, data -> {
            myReferrals.clear();
            myReferrals.addAll(data);
            updateUI();
        });
    }

    private void updateUI() {
        if (myReferrals.size() == 0) {
            referral1Layout.setVisibility(View.GONE);
            referral2Layout.setVisibility(View.GONE);
            return;
        }
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        if (myReferrals.size() >= 1) {
            referral1Layout.setVisibility(View.VISIBLE);
            referral2Layout.setVisibility(View.GONE);
            User user1 = myReferrals.get(0);
            referral1NameTxt.setText(user1.getName());
            referral1EmailTxt.setText(user1.getEmail());
            referral1JoinedDataTxt.setText(simpleDateFormat.format(user1.getJoinedDate()));
            referral1LevelTxt.setText(user1.getLevel());
            firestore.collection("user").whereEqualTo("referredByCode", user1.getReferralCode())
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.size() == 2) {
                            active1.setVisibility(View.VISIBLE);
                        }
                    });
        }
        if (myReferrals.size() == 2) {
            referral2Layout.setVisibility(View.VISIBLE);
            User user2 = myReferrals.get(1);
            referral2NameTxt.setText(user2.getName());
            referral2EmailTxt.setText(user2.getEmail());
            referral2JoinedDataTxt.setText(simpleDateFormat.format(user2.getJoinedDate()));
            referral2LevelTxt.setText(user2.getLevel());
            firestore.collection("user").whereEqualTo("referredByCode", user2.getReferralCode())
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.size() == 2) {
                            active2.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }
}
