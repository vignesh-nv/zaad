package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageActivity extends AppCompatActivity {

    Button saveBtn;

    FirebaseFirestore firestore;

    String language;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        firestore = FirebaseFirestore.getInstance();
        saveBtn = findViewById(R.id.save_btn);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("user").document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    setupLanguageDropDown();
                });
        saveBtn.setOnClickListener(view -> {
            if (language == null) {
                return;
            }
            firestore.collection("user").document(firebaseUser.getEmail()).update("language", language)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(LanguageActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void setupLanguageDropDown() {
        AutoCompleteTextView languageTextView;

        List<String> optionsList = Arrays.asList("English", "Tamil", "Telugu", "Malayalam", "Hindi", "Kannada");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_district_item,
                        optionsList);

        languageTextView =
                findViewById(R.id.language_dropdown);
        languageTextView.setAdapter(adapter);
        languageTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedOption = adapterView.getItemAtPosition(i).toString();
            language = selectedOption;
        });
        languageTextView.setText(user.getLanguage(), false);
    }
}