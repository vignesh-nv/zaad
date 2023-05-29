package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zaad.zaad.R;

public class SignupHomeActivity extends AppCompatActivity {

    Button signupBtn;

    TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_home);
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.login_txt);

        signupBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignupHomeActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignupHomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}