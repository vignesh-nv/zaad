package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.PAYMENT_COMPLETED;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.zaad.zaad.R;
import com.zaad.zaad.model.ReferralData;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentDetailActivity extends AppCompatActivity implements PaymentResultListener {

    Button payButton;
    TextInputEditText referralCodeTxt;
    LoginRegisterViewModel loginRegisterViewModel;
    private User user;
    private String referralCode, myReferralCode;
    FirebaseFirestore firestore;

    ReferralData referralData;

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
            if (checkReferralCode()) {
                validateReferralCode();
            }
        });
    }

    private void validateReferralCode() {
        // TODO: Call this logic once the payment is successful
        referralCode = referralCodeTxt.getText().toString();
        firestore.collection("referralCode").document(referralCode)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        referralData = documentSnapshot.toObject(ReferralData.class);
                        checkIfNotUsedMoreThanAllowed();
                    } else {
                        Toast.makeText(PaymentDetailActivity.this, "Invalid Referral Code", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfNotUsedMoreThanAllowed() {
        firestore.collection("user").whereEqualTo("referredByCode", referralCode)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.getDocuments().size() >= 2) {
                        Toast.makeText(this, "Referral Code cannot be used", Toast.LENGTH_SHORT).show();
                    } else {
                        createAndSaveReferralCode();
                        startPayment();
                    }
                });
    }

    private void saveReferralData(final String code) {
        ReferralData referralData = new ReferralData();
        referralData.setEmailId(user.getEmail());
        referralData.setReferralCode(code);
        loginRegisterViewModel.saveReferralData(referralData);
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.new_app_logo);

        checkout.setKeyID("rzp_test_qhd4HWdr10XmWx");
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Khanzoplay");
            options.put("currency", "INR");
            options.put("amount", 12100);
            checkout.open(this, options);
        } catch (Exception e) {
            Log.e("PaymentDetail", e.toString());
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PaymentDetailActivity.this, HomeActivity.class);
        saveUserDetails();
        updatePaymentCompleted();
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
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

    private boolean checkReferralCode() {
        if (isEmpty(referralCodeTxt.getText().toString())) {
            referralCodeTxt.setError("Referral Code is empty");
            return false;
        }
        return true;
    }

    private void updatePaymentCompleted() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PAYMENT_COMPLETED, true);
        editor.apply();
    }

    private void saveUserDetails() {
        long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
        user.setReferralCode(myReferralCode);
        user.setReferredByCode(referralCode);
        user.setJoinedDate(new Date());
        user.setPaymentCompleted(true);
        user.setLevel("A");
        user.setAmount(121);
        Date expiryDate = new Date();
        expiryDate.setYear(expiryDate.getYear() + 1);
        expiryDate = new Date(expiryDate.getTime() - MILLIS_IN_A_DAY);
        user.setExpiryDate(expiryDate);
        loginRegisterViewModel.saveUser(user);
    }

    private boolean isEmpty(String value) {
        return value == null || value.equals("");
    }
}
