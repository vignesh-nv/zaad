package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.PAYMENT_COMPLETED;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentOptionCallback;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.paymentsheet.PaymentSheetResultCallback;
import com.zaad.zaad.R;
import com.zaad.zaad.model.ReferralData;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PaymentDetailActivity extends AppCompatActivity implements PaymentResultListener {

    Button payButton;
    TextInputEditText referralCodeTxt, referNameTxt;
    LoginRegisterViewModel loginRegisterViewModel;
    private User user;
    private String referralCode, myReferralCode, creditBy;
    FirebaseFirestore firestore;

    ReferralData referralData;
    PaymentSheet.CustomerConfiguration customerConfig;

    private PaymentSheet.FlowController flowController;

    String paymentIntentClientSecret;

    PaymentSheet paymentSheet;

    TextView trailBtn;
    Button skipBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        payButton = findViewById(R.id.payButton);
        referralCodeTxt = findViewById(R.id.referralCodeTxt);
        referNameTxt = findViewById(R.id.referralNameTxt);
        skipBtn = findViewById(R.id.btn_skip);

        firestore = FirebaseFirestore.getInstance();

        Checkout.preload(getApplicationContext());
        user = (User) getIntent().getSerializableExtra("USER");

        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        payButton.setOnClickListener(view -> {
            if (checkReferralCode()) {
                validateReferralCode();
            }
        });
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        skipBtn.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentDetailActivity.this, HomeActivity.class);
            saveUserDetails(false);
            updatePaymentCompleted(false);
            startActivity(intent);
        });
    }

    private void showStripePaymentSheet() {
        final PaymentSheet.GooglePayConfiguration googlePayConfiguration =
                new PaymentSheet.GooglePayConfiguration(
                        PaymentSheet.GooglePayConfiguration.Environment.Production,
                        "IN"
                );
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Khanzoplay")
                .customer(customerConfig)
                // Set `allowsDelayedPaymentMethods` to true if your business can handle payment methods
                // that complete payment after a delay, like SEPA Debit and Sofort.
                .allowsDelayedPaymentMethods(true)
                .googlePay(googlePayConfiguration)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PaymentDetailActivity.this, HomeActivity.class);
            saveUserDetails();
            updatePaymentCompleted(true);
            startActivity(intent);
        }
    }

    private void validateReferralCode() {
        // TODO: Call this logic once the payment is successful
        referralCode = referralCodeTxt.getText().toString();
        creditBy = referNameTxt.getText().toString();

        if (referralCode.equals("KHANZO")) {
            createAndSaveReferralCode();
            startStripePayment();
            return;
        }
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
//                        createAndSaveReferralCode();
//                        startPayment();
                        startStripePayment();
                    }
                });
    }

    private void saveReferralData(final String code) {
        ReferralData referralData = new ReferralData();
        referralData.setEmailId(user.getEmail());
        referralData.setReferralCode(code);
        loginRegisterViewModel.saveReferralData(referralData);
    }

    private void startStripePayment() {
        Fuel.INSTANCE.post("https://us-central1-zaad-cb167.cloudfunctions.net/stripePayment", null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    final JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));
                    runOnUiThread(() -> showStripePaymentSheet());
                } catch (JSONException e) { /* handle error */
                    Toast.makeText(PaymentDetailActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void failure(@NonNull FuelError fuelError) {
                Toast.makeText(PaymentDetailActivity.this, "Error O" + fuelError.getErrorData(), Toast.LENGTH_SHORT).show();
            }
        });
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
        updatePaymentCompleted(true);
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

    private void updatePaymentCompleted(boolean completed) {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PAYMENT_COMPLETED, completed);
        editor.apply();
    }

    private void saveUserDetails() {
        long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
        user.setReferralCode(myReferralCode);
        user.setReferredByCode(referralCode);
        user.setJoinedDate(new Date());
        user.setPaymentCompleted(true);
        user.setLevel("A");
        user.setAmount(100);
        user.setCreditBy(creditBy);
        Date expiryDate = new Date();
        expiryDate.setYear(expiryDate.getYear() + 1);
        expiryDate = new Date(expiryDate.getTime() - MILLIS_IN_A_DAY);
        user.setExpiryDate(expiryDate);
        user.setCreditByAmountEarned(0);
        user.setCreditByReferralCount(0);
        loginRegisterViewModel.saveUser(user);
        addAmountToCreditBy();
    }

    private void saveUserDetails(final boolean paymentCompleted) {
        long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
        user.setReferralCode(myReferralCode);
        user.setReferredByCode(referralCode);
        user.setJoinedDate(new Date());
        user.setPaymentCompleted(paymentCompleted);
        user.setLevel("A");
        user.setAmount(0);
        user.setCreditBy(creditBy);
        Date expiryDate = new Date();
        expiryDate.setYear(expiryDate.getYear() + 1);
        expiryDate = new Date(expiryDate.getTime() - MILLIS_IN_A_DAY);
        user.setExpiryDate(expiryDate);
        user.setCreditByAmountEarned(0);
        user.setCreditByReferralCount(0);
        loginRegisterViewModel.saveUser(user);
    }

    private boolean isEmpty(String value) {
        return value == null || value.equals("");
    }

    private void addAmountToCreditBy() {
        if (creditBy == null || creditBy.equals("")) {
            return;
        }
        firestore.collection("user").whereEqualTo("referralCode", creditBy)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                        User user1 = snapshot.toObject(User.class);
                        if (!Objects.equals(user1.getLevel(), "A")) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("amount", user1.getAmount() + 50);
                            map.put("creditByReferralCount", user1.getCreditByReferralCount() + 1);
                            map.put("creditByAmountEarned", user1.getCreditByAmountEarned() + 50);
                            loginRegisterViewModel.updateUserWithID(user1.getEmail(), map);
                        }
                    }
                });

    }
}
