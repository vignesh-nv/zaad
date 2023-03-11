package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.MyLevelViewModel;

public class MyLevelActivity extends AppCompatActivity {

    TextView referralCodeTxt;
    MyLevelViewModel myLevelViewModel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_level);

        referralCodeTxt = findViewById(R.id.referralCodeTxt);
        myLevelViewModel = new ViewModelProvider(this).get(MyLevelViewModel.class);

        myLevelViewModel.getUser().observe(this, data -> {
            user = data;
            referralCodeTxt.setText(user.getReferralCode());
        });
    }
}
