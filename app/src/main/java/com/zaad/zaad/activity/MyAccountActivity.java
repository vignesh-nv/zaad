package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.MyAccountViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyAccountActivity extends AppCompatActivity {

    private User user;
    TextView userNameTxt, phoneNumberTxt, bankNameTxt, customerNameTxt, accountNumberTxt;
    TextView ifscCodeTxt, upiTxt, addressTxt, joinedDateTxt, expiryDateTxt, pincodeTxt;
    ImageButton editProfileBtn;
    Button logoutBtn;

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
        logoutBtn = findViewById(R.id.logoutBtn);
        joinedDateTxt = findViewById(R.id.joinedDateTxt);
        expiryDateTxt = findViewById(R.id.expiryDateTxt);
        pincodeTxt = findViewById(R.id.pincodeTxt);

        MyAccountViewModel accountViewModel =
                new ViewModelProvider(this).get(MyAccountViewModel.class);
        accountViewModel.getUser().observe(this, data -> {
            user = data;
            updateUI();
        });

        editProfileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MyAccountActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MyAccountActivity.this, SignupHomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateUI() {
        userNameTxt.setText(user.getName());
        if (user.getAccountDetails() != null) {
            accountNumberTxt.setText(user.getAccountDetails().getAccountNumber());
            customerNameTxt.setText(user.getAccountDetails().getAccountHolderName());
            bankNameTxt.setText(user.getAccountDetails().getBankName());
            upiTxt.setText(user.getAccountDetails().getUpi());
        }
        addressTxt.setText(user.getAddress());
        pincodeTxt.setText(user.getPincode());
        phoneNumberTxt.setText(user.getPhoneNumber());
        joinedDateTxt.setText(parseDate(user.getJoinedDate()));
        expiryDateTxt.setText(parseDate(user.getExpiryDate()));
    }

    private String parseDate(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}