package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.UiAutomation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaad.zaad.R;
import com.zaad.zaad.model.State;
import com.zaad.zaad.model.User;
import com.zaad.zaad.utils.AppUtils;
import com.zaad.zaad.viewmodel.LoginRegisterViewModel;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PersonalDetailsActivity extends AppCompatActivity {

    TextView phoneNumberTxt, addressTxt, pincodeTxt, nameTxt, dateTxt;
    Button signupBtn;
    private String phoneNumber, gender, district, language, state;

    private Date dob;
    RadioGroup genderRG;
    RadioButton maleRB, femaleRB;
    User user;

    Map<String, List<String>> stateDistrictMap = new HashMap<>();
    List<String> stateList = new ArrayList<>();

    List<String> districtList = new ArrayList<>();
    AutoCompleteTextView stateTextView, districtTextView, languageTextView;
    ArrayAdapter<String> districtAdapter;

    TextInputLayout languageDropdownLayout, stateDropdownLayout, districtDropdownLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        user = (User) getIntent().getSerializableExtra("USER");
        phoneNumberTxt = findViewById(R.id.phoneNumberTxt);
        addressTxt = findViewById(R.id.addressTxt);
        pincodeTxt = findViewById(R.id.pincodeTxt);
        signupBtn = findViewById(R.id.signupBtn);
        nameTxt = findViewById(R.id.nameTxt);
        dateTxt = findViewById(R.id.dateField);
        genderRG = findViewById(R.id.gender_rg);
        maleRB = findViewById(R.id.male_rb);
        femaleRB = findViewById(R.id.female_rb);
        languageDropdownLayout = findViewById(R.id.language_dropdown_layout);
        stateDropdownLayout = findViewById(R.id.state_drop_down_layout);
        districtDropdownLayout = findViewById(R.id.district_drop_down_layout);

        signupBtn.setOnClickListener(view -> {
            if (checkEmptyFields()) {
                Intent intent = new Intent(PersonalDetailsActivity.this, AccountDetailsActivity.class);
                User user = prepareUserData();
                intent.putExtra("USER", user);
                startActivity(intent);
            }
        });

        readStatesAndDistrictData();
        setupStateSpinner();
        setupDistrictSpinner();
        setupLanguageDropDown();
        displayUserData();
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
                dob = new Date(selection);
            });
        });
    }

    private void setupStateSpinner() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_district_item,
                        stateList);

        stateTextView =
                findViewById(R.id.state_drop_down);
        stateTextView.setAdapter(adapter);
        stateTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            stateDropdownLayout.setHintEnabled(false);
            String selectedOption = adapterView.getItemAtPosition(i).toString();
            if (districtTextView.getAdapter() == null) {
                districtTextView.setAdapter(districtAdapter);
            }
            districtList.clear();
            districtList.addAll(stateDistrictMap.get(selectedOption));
            districtAdapter.notifyDataSetChanged();
            district = "";
            state = selectedOption;
            districtTextView.setText("", false);
        });
    }

    private void setupDistrictSpinner() {
        districtAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_district_item,
                        districtList);

        districtTextView =
                findViewById(R.id.filled_exposed_dropdown);
        districtTextView.setAdapter(districtAdapter);
        districtTextView.setAdapter(null);
        districtTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            districtDropdownLayout.setHintEnabled(false);
            district = adapterView.getItemAtPosition(i).toString();
        });
    }

    private void setupLanguageDropDown() {
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
            languageDropdownLayout.setHintEnabled(false);
        });
    }

    private void displayUserData() {
        if (user != null) {
            nameTxt.setText(user.getName());
            addressTxt.setText(user.getAddress());
            pincodeTxt.setText(user.getPincode());
            phoneNumberTxt.setText(user.getPhoneNumber());
            if (user.getGender() != null && user.getGender().equals("Male")) {
                maleRB.setChecked(true);
            } else if (user.getGender() != null && user.getGender().equals("Female")) {
                femaleRB.setChecked(true);
            }
            stateTextView.setText(user.getState(), false);
            languageTextView.setText(user.getLanguage(), false);
            districtTextView.setText(user.getDistrict(), false);
        }
    }

    private User prepareUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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
        user.setDob(dob);
        user.setDistrict(district);
        user.setState(state);
        user.setLanguage(language);
        user.setEmail(firebaseUser.getEmail());
        return user;
    }


    private boolean checkEmptyFields() {

        if (isEmpty(nameTxt.getText().toString())) {
            nameTxt.setError("Name is empty");
            return false;
        }

        if (isEmpty(phoneNumberTxt.getText().toString())) {
            phoneNumberTxt.setError("Phone Number is empty");
            return false;
        }

        if (phoneNumberTxt.getText().toString().length() != 10) {
            phoneNumberTxt.setError("Phone Number is invalid");
            return false;
        }

        if (isEmpty(dateTxt.getText().toString())) {
            dateTxt.setError("DOB is empty");
            return false;
        }

        if (isEmpty(addressTxt.getText().toString())) {
            addressTxt.setError("Address is empty");
            return false;
        }

        if (isEmpty(pincodeTxt.getText().toString())) {
            pincodeTxt.setError("Pincode is empty");
            return false;
        }

        if (isEmpty(district)) {
            Toast.makeText(this, "Select district", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isEmpty(language)) {
            Toast.makeText(this, "Select Language", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void readStatesAndDistrictData() {
        String jsonString = AppUtils.getJsonFromAssets(this, "districts.json");
        Gson gson = new Gson();
        Type listStateType = new TypeToken<List<State>>() {}.getType();

        Log.i("JsonString", "" + jsonString);
        List<State> states = gson.fromJson(jsonString, listStateType);

        for (State state : states) {
            stateDistrictMap.put(state.getName(), state.getDistricts());
            stateList.add(state.getName());
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.equals("");
    }
}