package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.PAYMENT_COMPLETED;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zaad.zaad.R;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    TextInputEditText emailTxt, passwordTxt;
    TextView forgotPasswordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.loginBtn);
        emailTxt = findViewById(R.id.email_edit_text);
        passwordTxt = findViewById(R.id.password_edit_text);
        forgotPasswordTxt = findViewById(R.id.forgot_password_txt);
        forgotPasswordTxt.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
            startActivity(intent);
        });
        loginBtn.setOnClickListener(view -> {
            loginUser();
        });
    }

    private void loginUser() {
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkIfEmailVerified();
                    } else {
                        Log.w("TAG", "signInWithEmail:failed", task.getException());
                        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {
            if (checkIsPaymentCompleted()) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, PersonalDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, "Verify the email", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }
    }

    private boolean checkIsPaymentCompleted() {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        boolean paymentCompleted = sharedPref.getBoolean(PAYMENT_COMPLETED, false);
        return paymentCompleted;
    }

}
