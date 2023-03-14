package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.USER_EMAIL;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;

public class EmailSignupAcitivity extends AppCompatActivity {

    String TAG = "EmailSignupActivity";
    Button signupBtn;
    TextInputEditText passwordTxt;
    String email;
    FirebaseAuth auth;
    boolean isValidUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup_acitivity);
        signupBtn = findViewById(R.id.signupBtn);
        passwordTxt = findViewById(R.id.password_edit_text);

        signupBtn.setOnClickListener(view -> {
            if (isValidUser)
                linkEmailAuthentication();
            else
                Toast.makeText(this, "Invalid User", Toast.LENGTH_SHORT).show();
        });
        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String emailLink = intent.getData().toString();

        if (auth.isSignInWithEmailLink(emailLink)) {
            SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            email = sharedPref.getString(USER_EMAIL, "");
            if (email == null || email.equals("")) {
                Toast.makeText(this, "Invalid User", Toast.LENGTH_SHORT).show();
                return;
            }
            isValidUser = true;
            // The client SDK will parse the code from the link for you.
//            auth.signInWithEmailLink(email, emailLink)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "Successfully signed in with email link!");
//                            AuthResult result = task.getResult();
//                        } else {
//                            Log.e(TAG, "Error signing in with email link", task.getException());
//                        }
//                    });
        }
    }

    private void linkEmailAuthentication() {
        String password = passwordTxt.getText().toString();
        User user = new User();
        user.setEmail(email);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "linkWithCredential:success");
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        Toast.makeText(EmailSignupAcitivity.this, "Signup was successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EmailSignupAcitivity.this, PersonalDetailsActivity.class);
                        intent.putExtra("USER", user);
                        startActivity(intent);
                    } else {
                        Log.w(TAG, "linkWithCredential:failure", task.getException());
                        Toast.makeText(EmailSignupAcitivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}