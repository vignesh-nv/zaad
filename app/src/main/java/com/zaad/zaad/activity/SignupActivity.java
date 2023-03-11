package com.zaad.zaad.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
    LoginRegisterViewModel loginRegisterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        sign_in();
    }

    public void sign_in() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null) {
//            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
//            startActivity(intent);
//            Toast.makeText(getApplicationContext(), "User already signed", Toast.LENGTH_SHORT).show();
//        } else {
            //Choosing Authentication provider
            List<AuthUI.IdpConfig> providers= Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

            final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                    new FirebaseAuthUIActivityResultContract(),
                    result -> onSignInResult(result)
            );

            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
//        }
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Log.i(TAG, user.toString());
            Log.i(TAG, user.getPhoneNumber());
            Log.i(TAG, "onSignInResult: " + user.getDisplayName());
            String phoneNumber = user.getPhoneNumber().substring(3);

            Log.i("phoneNumber", phoneNumber);

            Intent intent = new Intent(SignupActivity.this, PersonalDetailsActivity.class);
            intent.putExtra("PHONE_NUMBER", phoneNumber);
            startActivity(intent);
        } else {
            Log.e(TAG, "onSignInResult: " + response.getError().getErrorCode());
        }
    }
}