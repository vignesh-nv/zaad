package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.PAYMENT_COMPLETED;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;

import java.util.Arrays;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(this::checkChildMode, 2000);
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
        }
        finish();
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
                    Log.i("User", user.toString());
                    if (user.isPaymentCompleted()) {
                        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, PersonalDetailsActivity.class);
                        startActivity(intent);
                    }
                });
    }
}