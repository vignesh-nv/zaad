package com.zaad.zaad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.zaad.zaad.R;

public class PasswordResetActivity extends AppCompatActivity {

    TextInputEditText emailTxt;
    Button resetPasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        emailTxt = findViewById(R.id.email_edit_text);

        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        resetPasswordBtn.setOnClickListener(view -> {
            sendResetMail();
        });
    }

    private void sendResetMail() {
        String email = emailTxt.getText().toString();
        if (email == null || email.equals("")) {
            Toast.makeText(this, "Enter the Email", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("", "Email sent.");
                        Toast.makeText(PasswordResetActivity.this, "Mail has been sent. Check your inbox", Toast.LENGTH_SHORT).show();
                    } else {
                        Exception e = task.getException();
                        Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}