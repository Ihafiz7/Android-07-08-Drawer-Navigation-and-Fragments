package com.hafiz.myalmary.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.hafiz.myalmary.R;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private TextInputEditText editTextName, editTextEmail, editTextPhone, editTextDate, editTextAddress;
    private AutoCompleteTextView autoCompleteGender;
    private MaterialButton buttonSubmit, buttonClear, buttonNavigate;
    private SharedPreferences sharedPreferences;

    // SharedPreferences keys
    private static final String PREFS_NAME = "UserProfilePrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATE = "date";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_GENDER = "gender";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupSharedPreferences();
        loadSavedData();
        setupGenderDropdown();
        setupDatePicker();
        setupButtonListeners();
    }

    private void initializeViews(View view) {
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        autoCompleteGender = view.findViewById(R.id.autoCompleteGender);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonClear = view.findViewById(R.id.buttonClear);
        buttonNavigate = view.findViewById(R.id.buttonNavigate);
    }

    private void setupSharedPreferences() {
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private void loadSavedData() {
        editTextName.setText(sharedPreferences.getString(KEY_NAME, ""));
        editTextEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));
        editTextPhone.setText(sharedPreferences.getString(KEY_PHONE, ""));
        editTextDate.setText(sharedPreferences.getString(KEY_DATE, ""));
        editTextAddress.setText(sharedPreferences.getString(KEY_ADDRESS, ""));
        autoCompleteGender.setText(sharedPreferences.getString(KEY_GENDER, ""));
    }

    private void setupGenderDropdown() {
        String[] genders = {"Male", "Female", "Other", "Prefer not to say"};

        // Create adapter with proper styling
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                genders
        );

        autoCompleteGender.setAdapter(adapter);

        // Fix: Set threshold to 1 to show dropdown when typing starts
        autoCompleteGender.setThreshold(1);

        // Fix: Make sure the dropdown works on click
        autoCompleteGender.setOnClickListener(v -> {
            autoCompleteGender.showDropDown();
        });

        // Fix: Also show dropdown when focused
        autoCompleteGender.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                autoCompleteGender.showDropDown();
            }
        });
    }

    private void setupDatePicker() {
        editTextDate.setOnClickListener(v -> showDatePicker());

        // Also make it focusable=false to prevent keyboard
        editTextDate.setFocusable(false);
        editTextDate.setClickable(true);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editTextDate.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void setupButtonListeners() {
        buttonSubmit.setOnClickListener(v -> submitForm());
        buttonClear.setOnClickListener(v -> clearForm());
        buttonNavigate.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.profileFragment));
    }

    private void submitForm() {
        if (validateForm()) {
            saveFormData();
            String name = editTextName.getText().toString();

            Snackbar.make(requireView(), "Profile saved successfully!", Snackbar.LENGTH_LONG)
                    .setAction("VIEW PROFILE", v ->
                            Navigation.findNavController(v).navigate(R.id.profileFragment))
                    .show();
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (editTextName.getText().toString().trim().isEmpty()) {
            editTextName.setError("Please enter your name");
            isValid = false;
        } else {
            editTextName.setError(null);
        }

        if (editTextEmail.getText().toString().trim().isEmpty()) {
            editTextEmail.setError("Please enter your email");
            isValid = false;
        } else {
            editTextEmail.setError(null);
        }

        return isValid;
    }

    private void saveFormData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, editTextName.getText().toString().trim());
        editor.putString(KEY_EMAIL, editTextEmail.getText().toString().trim());
        editor.putString(KEY_PHONE, editTextPhone.getText().toString().trim());
        editor.putString(KEY_DATE, editTextDate.getText().toString().trim());
        editor.putString(KEY_ADDRESS, editTextAddress.getText().toString().trim());
        editor.putString(KEY_GENDER, autoCompleteGender.getText().toString().trim());
        editor.apply();
    }

    private void clearForm() {
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
        editTextDate.setText("");
        editTextAddress.setText("");
        autoCompleteGender.setText("");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Snackbar.make(requireView(), "Form cleared", Snackbar.LENGTH_SHORT).show();
    }
}