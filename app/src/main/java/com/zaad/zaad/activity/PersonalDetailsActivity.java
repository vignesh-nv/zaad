package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

public class PersonalDetailsActivity extends AppCompatActivity {

    TextView emailAddressTxt, addressTxt, pincodeTxt, nameTxt;
    Button signupBtn;
    LoginRegisterViewModel loginRegisterViewModel;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");

        emailAddressTxt = findViewById(R.id.emailAddressTxt);
        addressTxt = findViewById(R.id.addressTxt);
        pincodeTxt = findViewById(R.id.pincodeTxt);
        signupBtn = findViewById(R.id.signupBtn);
        nameTxt = findViewById(R.id.nameTxt);

//        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);

        signupBtn.setOnClickListener(view -> {

            Intent intent = new Intent(PersonalDetailsActivity.this, AccountDetailsActivity.class);
            User user = prepareUserData();
            intent.putExtra("PHONE_NUMBER", phoneNumber);
            intent.putExtra("USER", user);
            startActivity(intent);
        });
    }

    private User prepareUserData() {
        String email = emailAddressTxt.getText().toString();
        String address = addressTxt.getText().toString();
        String pincode = pincodeTxt.getText().toString();
        String name = nameTxt.getText().toString();

        User user = new User();
        user.setEmail(email);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        user.setPincode(pincode);
        user.setName(name);
        return user;
    }
}