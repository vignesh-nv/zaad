package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zaad.zaad.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(this::checkChildMode, 2000);
    }

    private void checkChildMode() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        boolean childMode = sharedPref.getBoolean(CHILD_MODE, false);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (childMode) {
            Intent intent = new Intent(SplashScreenActivity.this, ChildModeActivity.class);
            startActivity(intent);
        } else if (firebaseUser != null){
            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, SignupActivity.class);
            startActivity(intent);
        }
        finish();
    }
}