package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.SUBSCRIBED_TO_TOPIC;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        subscribeToTopic();
        new Handler().postDelayed(this::checkChildMode, 1000);
    }

    private void checkChildMode() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        boolean childMode = sharedPref.getBoolean(CHILD_MODE, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (childMode) {
            Intent intent = new Intent(SplashScreenActivity.this, ChildModeActivity.class);
            startActivity(intent);
        } else if (firebaseUser != null) {
            checkEmailAndPaymentStatus();
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, SignupHomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkEmailAndPaymentStatus() {
        Log.i("FirebaseUser", firebaseUser.getEmail());
        if (!firebaseUser.isEmailVerified()) {
            Toast.makeText(this, "Verify the Email", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null && user.isPaymentCompleted()) {
                        Intent intent = new Intent(SplashScreenActivity.this, PersonalDetailsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, PersonalDetailsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void subscribeToTopic() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        boolean subscribed = sharedPref.getBoolean(SUBSCRIBED_TO_TOPIC, false);
        if (subscribed) {
            return;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SUBSCRIBED_TO_TOPIC, true);
        editor.apply();
        FirebaseMessaging.getInstance().subscribeToTopic("withdrawal")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
                    Log.d("SplashScreenActivity", msg);
                });
    }
}