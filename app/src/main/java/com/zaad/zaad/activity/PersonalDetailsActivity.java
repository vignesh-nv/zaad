package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.UiAutomation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PersonalDetailsActivity extends AppCompatActivity {

    TextView phoneNumberTxt, addressTxt, pincodeTxt, nameTxt, dateTxt;
    Button signupBtn;
    LoginRegisterViewModel loginRegisterViewModel;
    private String phoneNumber, gender;

    private Date dob;
    RadioGroup genderRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");

        phoneNumberTxt = findViewById(R.id.phoneNumberTxt);
        addressTxt = findViewById(R.id.addressTxt);
        pincodeTxt = findViewById(R.id.pincodeTxt);
        signupBtn = findViewById(R.id.signupBtn);
        nameTxt = findViewById(R.id.nameTxt);
        dateTxt = findViewById(R.id.dateField);

//        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);

        signupBtn.setOnClickListener(view -> {
            checkEmptyFields();
//            Intent intent = new Intent(PersonalDetailsActivity.this, AccountDetailsActivity.class);
//            User user = prepareUserData();
//            intent.putExtra("PHONE_NUMBER", phoneNumber);
//            intent.putExtra("USER", user);
//            startActivity(intent);
        });

        genderRG.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.male_rb) {
                gender = "Male";
            } else if (i == R.id.female_rb) {
                gender = "Female";
            }
        });

        dateTxt.setOnClickListener(view -> {
            MaterialDatePicker<Long> datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Date Of Birth")
                            .build();
            datePicker.show(getSupportFragmentManager(), "DOB");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = sdf.format(selection);
                dateTxt.setText(date);
            });
        });
    }

    private User prepareUserData() {
        String phoneNumber = phoneNumberTxt.getText().toString();
        String address = addressTxt.getText().toString();
        String pincode = pincodeTxt.getText().toString();
        String name = nameTxt.getText().toString();

        User user = new User();
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        user.setPincode(pincode);
        user.setName(name);
        user.setGender(gender);
        return user;
    }


    private void checkEmptyFields() {
        if (isEmpty(phoneNumberTxt.getText().toString())) {
            phoneNumberTxt.setError("Phone Number is empty");
        }

        if (isEmpty(addressTxt.getText().toString())) {
            addressTxt.setError("Address is empty");
        }

        if (isEmpty(nameTxt.getText().toString())) {
            nameTxt.setError("Name is empty");
        }

        if (isEmpty(pincodeTxt.getText().toString())) {
            pincodeTxt.setError("Pincode is empty");
        }

        if (isEmpty(dateTxt.getText().toString())) {
            dateTxt.setError("DOB is empty");
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.equals("");
    }
}