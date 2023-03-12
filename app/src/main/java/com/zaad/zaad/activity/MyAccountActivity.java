package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.MyAccountViewModel;

public class MyAccountActivity extends AppCompatActivity {

    private User user;
    TextView userNameTxt, phoneNumberTxt, bankNameTxt, customerNameTxt, accountNumberTxt;
    TextView ifscCodeTxt, upiTxt, addressTxt;
    ImageButton editProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        userNameTxt = findViewById(R.id.userNameTxt);
        phoneNumberTxt = findViewById(R.id.phoneNumberTxt);
        bankNameTxt = findViewById(R.id.bankNameTxt);
        customerNameTxt = findViewById(R.id.accountHolderNameTxt);
        accountNumberTxt = findViewById(R.id.accountNumberTxt);
        upiTxt = findViewById(R.id.upiTxt);
        addressTxt = findViewById(R.id.address);
        editProfileBtn = findViewById(R.id.edit_profile_btn);

        MyAccountViewModel accountViewModel =
                new ViewModelProvider(this).get(MyAccountViewModel.class);
        accountViewModel.getUser().observe(this, data -> {
            user = data;
            updateUI();
            Log.i("MyAccountActivity", "userData: " + user);
        });

        editProfileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MyAccountActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }

    private void updateUI() {
        userNameTxt.setText(user.getName());
        if (user.getAccountDetails() != null) {
            accountNumberTxt.setText(user.getAccountDetails().getAccountNumber());
            customerNameTxt.setText(user.getAccountDetails().getAccountHolderName());
            bankNameTxt.setText(user.getAccountDetails().getBankName());
            addressTxt.setText(user.getAddress());
            upiTxt.setText(user.getAccountDetails().getUpi());
        }
    }
}