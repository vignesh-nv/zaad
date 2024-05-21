package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.PAYMENT_COMPLETED;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.UPIApplicationInfo;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.zaad.zaad.R;
import com.zaad.zaad.model.PaymentStatus;
import com.zaad.zaad.model.ReferralData;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        Checkout.preload(getApplicationContext());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        payButton.setOnClickListener(view -> {
            if (checkReferralCode()) {
                validateReferralCode();
            }
        });
        user = (User) getIntent().getSerializableExtra("USER");
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
//        PhonePe.init(this, PhonePeEnvironment.RELEASE, "M1H7XF81UMQX", "66264b911fb44238bcaec6bae21ddc16");
//        String string_signature = PhonePe.getPackageSignature();
//        System.out.println("Signature: " + string_signature);
    }

    private void validateReferralCode() {
        // TODO: Call this logic once the payment is successful
        referralCode = referralCodeTxt.getText().toString();
        creditBy = referNameTxt.getText().toString();

        if (referralCode.equals("KHANZO")) {
            createAndSaveReferralCode();
//            startStripePayment();
            startPayment();
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
                        startPayment();
//                        startStripePayment();
//                        startPhonePePayment();
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        saveUserDetails(true);
        updatePaymentCompleted(true);
        startActivity(intent);
        finish();
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

    private void startPhonePePayment() {
        try {
            PhonePe.setFlowId("39285639131"); // Recommended, not mandatory , An alphanumeric string without any special character
            List<UPIApplicationInfo> upiApps = PhonePe.getUpiApps();
            for (UPIApplicationInfo info : upiApps) {
                Log.d("CheckoutActivityApp", info.getApplicationName());
            }
        } catch (PhonePeInitException exception) {
            exception.printStackTrace();
        }
        JSONObject paymentData = new JSONObject();
        try {
            paymentData.put("merchantId", "M1H7XF81UMQX");
//            paymentData.put("merchantId", "PGTESTPAYUAT");
            paymentData.put("merchantTransactionId", "vignesh@gmail.com");
            paymentData.put("merchantUserId", "wdscdswa");
            paymentData.put("amount", 24200);
            paymentData.put("callbackUrl", "https://asia-south1-zaad-cb167.cloudfunctions.net/phonePeWebhook");
            paymentData.put("mobileNumber", "6381511648");

            JSONObject paymentInstrument = new JSONObject();
            paymentInstrument.put("type", "PAY_PAGE");

            paymentData.put("paymentInstrument", paymentInstrument);

            String base64Body = Base64.encodeToString(paymentData.toString().getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
            String checksum = generateSHA256Hash(base64Body + "/pg/v1/pay" + "abfd7a4b-73e6-4cd9-ae76-ea710ed4211e") + "###1"; // Prod
//            String checksum = generateSHA256Hash(base64Body + "/pg/v1/pay" + "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399") + "###1";
            B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                    .setData(base64Body)
                    .setChecksum(checksum)
                    .setUrl("/pg/v1/pay")
                    .build();
            PostPaymentActivity.this.startActivityForResult(PhonePe.getImplicitIntent(
                    PostPaymentActivity.this, b2BPGRequest, ""), 123);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String generateSHA256Hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = data.getBytes();
        digest.update(bytes, 0, bytes.length);
        byte[] hash = digest.digest();

        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.app_icon);

        checkout.setKeyID("rzp_live_1OlrQvIpReRBV6");
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Khanzoplay");
            options.put("currency", "INR");
            options.put("amount", 24200);
            checkout.open(this, options);
        } catch (Exception e) {
            Log.e("PaymentDetail", e.toString());
        }
    }

    //Phonepe
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        firestore.collection("payments").document("vignesh@gmail.com").get()
                .addOnSuccessListener(documentSnapshot -> {
                    PaymentStatus paymentStatus = documentSnapshot.toObject(PaymentStatus.class);
                    if (paymentStatus==null) {
                        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (paymentStatus.getStatus().equals("SUCCESS")) {
                        Toast.makeText(this, "Payment Completed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostPaymentActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        saveUserDetails(true);
                        updatePaymentCompleted(true);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostPaymentActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}