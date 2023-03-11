package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.zaad.zaad.R;

public class LoginActivity extends AppCompatActivity {

    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUpButton = findViewById(R.id.signupBtn);
        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, AccountDetailsActivity.class);
            startActivity(intent);
        });
    }
}