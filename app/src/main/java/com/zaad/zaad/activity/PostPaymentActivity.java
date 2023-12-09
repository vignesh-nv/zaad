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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.PaymentResultListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
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

public class PostPaymentActivity extends AppCompatActivity  implements PaymentResultListener {

    Button payButton;
    TextInputEditText referralCodeTxt, referNameTxt;
    LoginRegisterViewModel loginRegisterViewModel;
    private User user;

    private FirebaseUser firebaseUser;

    private String referralCode, myReferralCode, creditBy;
    FirebaseFirestore firestore;

    ReferralData referralData;
    PaymentSheet.CustomerConfiguration customerConfig;

    private PaymentSheet.FlowController flowController;
    String paymentIntentClientSecret;

    PaymentSheet paymentSheet;

    TextView trailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_payment);

        payButton = findViewById(R.id.payButton);
        referralCodeTxt = findViewById(R.id.referralCodeTxt);
        referNameTxt = findViewById(R.id.referralNameTxt);
        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        payButton.setOnClickListener(view -> {
            if (checkReferralCode()) {
                validateReferralCode();
            }
        });
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
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
                        Toast.makeText(PostPaymentActivity.this, "Invalid Referral Code", Toast.LENGTH_SHORT).show();
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
//                        startPayment();
                        startStripePayment();

                    }
                });
    }

    private void saveReferralData(final String code) {
        ReferralData referralData = new ReferralData();
        referralData.setEmailId(firebaseUser.getEmail());
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStripePaymentSheet();
                        }
                    });
                } catch (JSONException e) { /* handle error */
                    Toast.makeText(PostPaymentActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) {
                Toast.makeText(PostPaymentActivity.this, "Error O" + fuelError.getErrorData(), Toast.LENGTH_SHORT).show();
            }
        });
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
            Intent intent = new Intent(PostPaymentActivity.this, HomeActivity.class);
            saveUserDetails(true);
            updatePaymentCompleted(true);
            startActivity(intent);
        }
    }

    private void showStripePaymentSheet() {
        final PaymentSheet.GooglePayConfiguration googlePayConfiguration =
                new PaymentSheet.GooglePayConfiguration(
                        PaymentSheet.GooglePayConfiguration.Environment.Test,
                        "IN"
                );
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Example, Inc.")
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

    private void saveUserDetails(final boolean paymentCompleted) {
        long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
        Date expiryDate = new Date();
        expiryDate.setYear(expiryDate.getYear() + 1);
        expiryDate = new Date(expiryDate.getTime() - MILLIS_IN_A_DAY);

        Map<String, Object> updatedUserMap = new HashMap<>();
        updatedUserMap.put("referralCode", myReferralCode);
        updatedUserMap.put("referredByCode", referralCode);
        updatedUserMap.put("expiryDate", expiryDate);
        updatedUserMap.put("paymentCompleted", paymentCompleted);
        updatedUserMap.put("paymentDate", new Date());
        updatedUserMap.put("creditBy", creditBy);
        updatedUserMap.put("amount", 100);
        updatedUserMap.put("level", "A");

        loginRegisterViewModel.updateUser(updatedUserMap);
        addAmountToCreditBy();
    }

    private boolean isEmpty(String value) {
        return value == null || value.equals("");
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PostPaymentActivity.this, HomeActivity.class);
        saveUserDetails(true);
        updatePaymentCompleted(true);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }

    private void addAmountToCreditBy() {
        firestore.collection("user").whereEqualTo("referralCode", creditBy)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                        User user1 = snapshot.toObject(User.class);
                        if (!Objects.equals(user1.getLevel(), "A")) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("amount", user1.getAmount() + 50);
                            map.put("creditByReferralCount", user1.getCreditByReferralCount() + 1);
                            loginRegisterViewModel.updateUserWithID(user1.getEmail(), map);
                        }
                    }
                });

    }
}