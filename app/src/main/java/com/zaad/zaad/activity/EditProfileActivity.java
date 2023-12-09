package com.zaad.zaad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaad.zaad.R;
import com.zaad.zaad.model.State;
import com.zaad.zaad.model.User;
import com.zaad.zaad.utils.AppUtils;
import com.zaad.zaad.viewmodel.MyAccountViewModel;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    TextInputEditText fullNameTxt, emailTxt, phoneNumberTxt, addressTxt;
    TextInputEditText accountNumberTxt, accountHolderNameTxt, bankNameTxt, ifscTxt, upiTxt;

    MyAccountViewModel myAccountViewModel;
    private User user;
    private MaterialButton saveBtn;

    AutoCompleteTextView stateTextView, districtTextView;
    private String language;
    ArrayAdapter<String> districtAdapter;
    Map<String, List<String>> stateDistrictMap = new HashMap<>();
    List<String> stateList = new ArrayList<>();

    List<String> districtList = new ArrayList<>();

    String state, district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setupUI();
        myAccountViewModel = new ViewModelProvider(this).get(MyAccountViewModel.class);
        myAccountViewModel.getUser().observe(this, data -> {
            user = data;
            updateUI();
        });

        readStatesAndDistrictData();
        saveBtn.setOnClickListener(view -> updateUser());
    }

    private void setupLanguageDropDown() {
        List<String> optionsList = Arrays.asList("Tamil", "Telugu", "Malayalam", "Hindi", "Kannada", "English");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_district_item,
                        optionsList);

        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.language_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedOption = adapterView.getItemAtPosition(i).toString();
            language = selectedOption;
        });
        editTextFilledExposedDropdown.setText(user.getLanguage(), false);
    }

    private void setupStateDropDown() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_district_item,
                        stateList);

        stateTextView =
                findViewById(R.id.state_drop_down);
        stateTextView.setAdapter(adapter);
        stateTextView.setOnItemClickListener((adapterView, view, i, l) -> {
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
        stateTextView.setText(user.getState(), false);
    }

    private void setupDistrictDropDown() {
        districtList.addAll(stateDistrictMap.get(user.getState()));
        districtAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_district_item,
                        districtList);

        districtTextView =
                findViewById(R.id.district_drop_down);
        districtTextView.setAdapter(districtAdapter);
        districtTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            district = adapterView.getItemAtPosition(i).toString();
        });
        districtTextView.setText(user.getDistrict(), false);
    }

    private void setupUI() {
        fullNameTxt = findViewById(R.id.full_name_edit_text);
        emailTxt = findViewById(R.id.email_edit_text);
        phoneNumberTxt = findViewById(R.id.phone_edit_text);
        addressTxt = findViewById(R.id.address_edit_text);
        accountHolderNameTxt = findViewById(R.id.account_holder_name_edit_text);
        accountNumberTxt = findViewById(R.id.account_number_edit_text);
        bankNameTxt = findViewById(R.id.bank_name_edit_text);
        ifscTxt = findViewById(R.id.ifsc_code_edit_text);
        upiTxt = findViewById(R.id.upi_edit_text);
        saveBtn = findViewById(R.id.save_button);
    }

    private void updateUI() {
        setupLanguageDropDown();
        setupStateDropDown();
        setupDistrictDropDown();
        fullNameTxt.setText(user.getName());
        phoneNumberTxt.setText(user.getPhoneNumber());
        addressTxt.setText(user.getAddress());
        if (user.getAccountDetails() != null) {
            accountHolderNameTxt.setText(user.getAccountDetails().getAccountHolderName());
            accountNumberTxt.setText(user.getAccountDetails().getAccountNumber());
            bankNameTxt.setText(user.getAccountDetails().getBankName());
            ifscTxt.setText(user.getAccountDetails().getIfsc());
            upiTxt.setText(user.getAccountDetails().getUpi());
        }
    }

    private void updateUser() {
        Map<String, Object> updates = new HashMap<>();
        if (state != null && !user.getState().equals(state)) {
            if (district == null || district.equals("")) {
                Toast.makeText(this, "Select District", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String name = fullNameTxt.getText().toString();
        if (!name.equals(user.getName())) {
            updates.put("name", name);
        }

        String phoneNumber = phoneNumberTxt.getText().toString();
        if (!phoneNumber.equals(user.getPhoneNumber())) {
            updates.put("phoneNumber", phoneNumber);
        }
        String address = addressTxt.getText().toString();
        if (!address.equals(user.getAddress())) {
            updates.put("address", address);
        }
        String accountHolderName = accountHolderNameTxt.getText().toString();
        if (user.getAccountDetails() == null || !accountHolderName.equals(user.getAccountDetails().getAccountHolderName())) {
            updates.put("accountDetails.accountHolderName", accountHolderName);
        }

        String accountNumber = accountNumberTxt.getText().toString();
        if (user.getAccountDetails() == null || !accountNumber.equals(user.getAccountDetails().getAccountNumber())) {
            updates.put("accountDetails.accountNumber", accountNumber);
        }
        String bankName = bankNameTxt.getText().toString();
        if (user.getAccountDetails() == null || !bankName.equals(user.getAccountDetails().getBankName())) {
            updates.put("accountDetails.bankName", bankName);
        }
        String ifsc = ifscTxt.getText().toString();
        if (user.getAccountDetails() == null || !ifsc.equals(user.getAccountDetails().getIfsc())) {
            updates.put("accountDetails.ifsc", ifsc);
        }
        String upi = upiTxt.getText().toString();
        if (user.getAccountDetails() == null || !ifsc.equals(user.getAccountDetails().getUpi())) {
            updates.put("accountDetails.upi", upi);
        }
        if (language != null) {
            if (user.getLanguage() == null || !user.getLanguage().equals(language))
                updates.put("language", language);
        }
        if (state != null) {
            if (!state.equals(user.getState())) {
                updates.put("state", state);
            }
        }
        if (district != null) {
            if (!district.equals(user.getDistrict())) {
                updates.put("district", district);
            }
        }
        myAccountViewModel.updateUser(updates);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void readStatesAndDistrictData() {
        String jsonString = AppUtils.getJsonFromAssets(this, "districts.json");
        Gson gson = new Gson();
        Type listStateType = new TypeToken<List<State>>() {}.getType();
        List<State> states = gson.fromJson(jsonString, listStateType);

        for (State state : states) {
            stateDistrictMap.put(state.getName(), state.getDistricts());
            stateList.add(state.getName());
        }
    }
}