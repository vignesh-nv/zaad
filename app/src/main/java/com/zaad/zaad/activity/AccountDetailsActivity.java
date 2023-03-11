package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.zaad.zaad.R;
import com.zaad.zaad.model.AccountDetails;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

public class AccountDetailsActivity extends AppCompatActivity {

    Button signupBtn;
    TextInputEditText accountNumberTxt, accountHolderNameTxt, ifscCodeTxt, upiNumberTxt, bankNameTxt;
    TextView skipTxt;
    LoginRegisterViewModel loginRegisterViewModel;

    private User user;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        user = (User) getIntent().getSerializableExtra("USER");

        signupBtn = findViewById(R.id.signupBtn);
//        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);

        accountHolderNameTxt = findViewById(R.id.accountHolderNameTxt);
        accountNumberTxt = findViewById(R.id.accountNumberTxt);
        ifscCodeTxt = findViewById(R.id.ifscCodeTxt);
        upiNumberTxt = findViewById(R.id.upiNumberTxt);
        bankNameTxt = findViewById(R.id.bankNameEditTxt);
        skipTxt = findViewById(R.id.skipTxt);

        signupBtn.setOnClickListener(view -> {
            saveAccountDetails();
            Intent intent = new Intent(AccountDetailsActivity.this, PaymentDetailActivity.class);
            intent.putExtra("USER", user);
            startActivity(intent);
        });

        skipTxt.setOnClickListener(view -> {
            Intent intent = new Intent(AccountDetailsActivity.this, PaymentDetailActivity.class);
            intent.putExtra("USER", user);
            startActivity(intent);
        });
    }

    private void saveAccountDetails() {
        String accountNumber = accountNumberTxt.getText().toString();
        String accountHolderName = accountHolderNameTxt.getText().toString();
        String ifscCode = ifscCodeTxt.getText().toString();
        String upiNumber = upiNumberTxt.getText().toString();
        String bankName = bankNameTxt.getText().toString();

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setAccountNumber(accountNumber);
        accountDetails.setAccountHolderName(accountHolderName);
        accountDetails.setIfsc(ifscCode);
        accountDetails.setBankName(bankName);
        user.setAccountDetails(accountDetails);
    }
}
