package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;

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
        if (email.equals("")) {
            Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkIsPaymentCompleted();
                    } else {
                        Log.w("TAG", "signInWithEmail:failed", task.getException());
                        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIsPaymentCompleted() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!firebaseUser.isEmailVerified()) {
            Toast.makeText(this, "Verify the Email", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user!=null && user.isPaymentCompleted()) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, PersonalDetailsActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
