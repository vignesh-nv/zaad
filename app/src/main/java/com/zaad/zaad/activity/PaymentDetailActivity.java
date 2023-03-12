package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.zaad.zaad.R;
import com.zaad.zaad.model.ReferralData;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Random;

public class PaymentDetailActivity extends AppCompatActivity implements PaymentResultListener {

    Button payButton;
    TextInputEditText referralCodeTxt;
    LoginRegisterViewModel loginRegisterViewModel;
    private User user;
    private String referralCode, myReferralCode;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        payButton = findViewById(R.id.payButton);
        referralCodeTxt = findViewById(R.id.referralCodeTxt);

        firestore = FirebaseFirestore.getInstance();

        Checkout.preload(getApplicationContext());
        user = (User) getIntent().getSerializableExtra("USER");

        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        payButton.setOnClickListener(view -> {
            validateReferralCode();
        });
    }

    private void validateReferralCode() {
        // TODO: Call this logic once the payment is successful
        referralCode = referralCodeTxt.getText().toString();
        if (referralCode.equals("")) {
            createAndSaveReferralCode();
            startPayment();
            return;
        }
        firestore.collection("referralCode").document(referralCode)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        createAndSaveReferralCode();
                        startPayment();
                    } else {
                        Toast.makeText(PaymentDetailActivity.this, "Invalid Referral Code", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveReferralData(final String code) {
        ReferralData referralData = new ReferralData();
        referralData.setPhoneNumber("6381511648");
        referralData.setReferralCode(code);
        loginRegisterViewModel.saveReferralData(referralData);
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);

        checkout.setKeyID("rzp_live_0FD1FY1bVAtKpp");
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Zaad");
            options.put("currency", "INR");
            options.put("amount", "115");
            checkout.open(this, options);
        } catch (Exception e) {
            Log.e("PaymentDetail", e.toString());
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PaymentDetailActivity.this, HomeActivity.class);
        user.setReferralCode(myReferralCode);
        user.setReferredByCode(referralCode);
        loginRegisterViewModel.saveUser(user);
//        loginRegisterViewModel.addUserReferralData();
        // ReferredBy
        // userId
        // referralDate
        //

        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        Log.e("PaymentDetailActivity", s);
        user.setReferredByCode(referralCode);
        user.setReferralCode(myReferralCode);
        loginRegisterViewModel.saveUser(user);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void createAndSaveReferralCode() {
        int codeLength = 6;
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        myReferralCode = sb.toString();
        saveReferralData(myReferralCode);
    }
}
