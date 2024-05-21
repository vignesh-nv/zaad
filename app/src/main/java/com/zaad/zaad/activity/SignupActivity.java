package com.zaad.zaad.activity;

import static com.zaad.zaad.constants.AppConstant.CHILD_MODE;
import static com.zaad.zaad.constants.AppConstant.USER_EMAIL;
import static com.zaad.zaad.constants.AppConstant.ZAAD_SHARED_PREFERENCE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.BuildConfig;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
    LoginRegisterViewModel loginRegisterViewModel;

    TextInputEditText emailEditext, editPasswordTxt;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        emailEditext = findViewById(R.id.user_email_edit_text);
        editPasswordTxt = findViewById(R.id.user_password_edit_text);
        signup = findViewById(R.id.signupBtn);
        signup.setOnClickListener(view -> {
            checkIsNewEmail();
        });

        FirebaseAuth.AuthStateListener stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    sendVerificationEmail();
                }
            }
        };
    }


    public void sendSignupLinkToEmail() {

        String email = emailEditext.getText().toString();
        String password = editPasswordTxt.getText().toString();
        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password doesn't meet the requirement", Toast.LENGTH_SHORT).show();
            return;
        }
        saveEmailToSharedPreference(email);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendVerificationEmail();
                } else {
                    Toast.makeText(SignupActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
//                .setUrl("https://zaad-cb167.web.app")
//                .setHandleCodeInApp(true)
//                .setAndroidPackageName(BuildConfig.APPLICATION_ID, false, null)
//                .build();
//
//        auth.sendSignInLinkToEmail(email, actionCodeSettings)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        showEmailSentDialog();
//                        Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Log.i(TAG, task.getException().toString());
//                        Log.i(TAG, task.getResult().toString());
//                    }
//                });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut();
                        showEmailSentDialog();
                    } else {
                        overridePendingTransition(0, 0);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                    }
                });
    }

    private void showEmailSentDialog() {
        AlertDialog builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Khanzo Play")
                .setMessage("Verification mail has been sent to your mail")
                .setPositiveButton("ok", (dialogInterface, i) -> {
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                })
                .show();
    }

    private void saveEmailToSharedPreference(final String email) {
        SharedPreferences sharedPref = getSharedPreferences(ZAAD_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_EMAIL, email);
        editor.apply();
    }

    private void checkIsNewEmail() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("user").document(emailEditext.getText().toString())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Toast.makeText(SignupActivity.this, "Email already exist", Toast.LENGTH_SHORT).show();
                    } else {
                        sendSignupLinkToEmail();
                    }
                });
    }

    public void sign_in() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null) {
//            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
//            startActivity(intent);
//            Toast.makeText(getApplicationContext(), "User already signed", Toast.LENGTH_SHORT).show();
//        } else {
        //Choosing Authentication provider
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

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

    public boolean isValidPassword(String password) {
        // Define the regular expression for a valid password
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=\\S+$).{8,}$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(regex);

        // Match the password against the regular expression
        Matcher matcher = pattern.matcher(password);

        // Return true if the password matches the regular expression, otherwise false
        return matcher.matches();
    }
}