package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.MyLevelViewModel;

public class MyLevelActivity extends AppCompatActivity {

    TextView referralCodeTxt;
    MyLevelViewModel myLevelViewModel;
    private User user;

    Button shareBtn;

    TextView referral1NameTxt, referral1EmailTxt, referral1JoinedDataTxt, referral1LevelTxt;
    TextView referral2NameTxt, referral2EmailTxt, referral2JoinedDataTxt, referral2LevelTxt;
    View referral1Layout, referral2Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_level);

        shareBtn = findViewById(R.id.shareBtn);
        referralCodeTxt = findViewById(R.id.referralCodeTxt);
        myLevelViewModel = new ViewModelProvider(this).get(MyLevelViewModel.class);

        setupUI();

        myLevelViewModel.getUser().observe(this, data -> {
            user = data;
            referralCodeTxt.setText(user.getReferralCode());
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

        referral2Layout = findViewById(R.id.referral2Layout);
        referral2LevelTxt = findViewById(R.id.level_2);
        referral2JoinedDataTxt = findViewById(R.id.joined_date_2);
        referral2EmailTxt = findViewById(R.id.email_2);
        referral2NameTxt = findViewById(R.id.referral_user_name_2);
    }

    private void getMyReferrals() {
    }

    private void updateUI() {

    }
}
