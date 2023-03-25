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
import com.zaad.zaad.R;
import com.zaad.zaad.model.User;
import com.zaad.zaad.viewmodel.MyAccountViewModel;

import org.w3c.dom.Text;

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

    private String language;

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

        saveBtn.setOnClickListener(view -> updateUser());
    }

    private void setupLanguageDropDown() {
        List<String> optionsList = Arrays.asList("Tamil", "Telugu", "Malayalam", "Hindi", "Kannada");
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
        fullNameTxt.setText(user.getName());
        emailTxt.setText(user.getEmail());
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
        String name = fullNameTxt.getText().toString();
        if (!name.equals(user.getName())) {
            updates.put("name", name);
        }
        String email = emailTxt.getText().toString();
        if (!email.equals(user.getEmail())) {
            updates.put("email", email);
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
        if (user.getAccountDetails()== null || !accountHolderName.equals(user.getAccountDetails().getAccountHolderName())) {
            updates.put("accountDetails.accountHolderName", accountHolderName);
        }

        String accountNumber = accountNumberTxt.getText().toString();
        if (user.getAccountDetails()== null || !accountNumber.equals(user.getAccountDetails().getAccountNumber())) {
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
        if (!user.getLanguage().equals(language)) {
            updates.put("language", language);
        }
        myAccountViewModel.updateUser(updates);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}