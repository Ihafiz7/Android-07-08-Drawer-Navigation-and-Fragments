package com.hafiz.myalmary.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.hafiz.myalmary.R;
import com.hafiz.myalmary.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private TextView textProfile;
    private MaterialCardView cardProfile;
    private MaterialButton buttonEdit, buttonShare;
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupSharedPreferences();
        setupButtonListeners();
        displayProfileData();
    }

    private void initializeViews(View view) {
        textProfile = view.findViewById(R.id.text_profile);
        cardProfile = view.findViewById(R.id.card_profile);
        buttonEdit = view.findViewById(R.id.button_edit);
        buttonShare = view.findViewById(R.id.button_share);
    }

    private void setupSharedPreferences() {
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private void setupButtonListeners() {
        // Edit button - Navigate back to Home fragment to edit
        buttonEdit.setOnClickListener(v -> {
            // Navigate back to Home fragment
            requireActivity().onBackPressed();
        });

        // Share button - Share profile as text
        buttonShare.setOnClickListener(v -> {
            shareProfile();
        });
    }

    private void displayProfileData() {
        String name = sharedPreferences.getString(KEY_NAME, "Not provided");
        String email = sharedPreferences.getString(KEY_EMAIL, "Not provided");
        String phone = sharedPreferences.getString(KEY_PHONE, "Not provided");
        String date = sharedPreferences.getString(KEY_DATE, "Not provided");
        String address = sharedPreferences.getString(KEY_ADDRESS, "Not provided");
        String gender = sharedPreferences.getString(KEY_GENDER, "Not provided");

        // Check if any data exists
        boolean hasData = !name.equals("Not provided") || !email.equals("Not provided");

        if (hasData) {
            String profileText = createProfileText(name, email, phone, date, gender, address);
            textProfile.setText(profileText);
            cardProfile.setVisibility(View.VISIBLE);

            // Show buttons only when data exists
            buttonEdit.setVisibility(View.VISIBLE);
            buttonShare.setVisibility(View.VISIBLE);
        } else {
            textProfile.setText("No profile data found.\n\nPlease go to Home and fill out the form to see your profile here.");
            cardProfile.setVisibility(View.GONE);

            // Hide buttons when no data
            buttonEdit.setVisibility(View.GONE);
            buttonShare.setVisibility(View.GONE);
        }
    }

    private String createProfileText(String name, String email, String phone, String date, String gender, String address) {
        StringBuilder sb = new StringBuilder();
        sb.append("Profile Information\n\n");

        if (!name.equals("Not provided")) {
            sb.append("👤 Name: ").append(name).append("\n\n");
        }
        if (!email.equals("Not provided")) {
            sb.append("📧 Email: ").append(email).append("\n\n");
        }
        if (!phone.equals("Not provided")) {
            sb.append("📞 Phone: ").append(phone).append("\n\n");
        }
        if (!date.equals("Not provided")) {
            sb.append("🎂 Date of Birth: ").append(date).append("\n\n");
        }
        if (!gender.equals("Not provided")) {
            sb.append("⚧ Gender: ").append(gender).append("\n\n");
        }
        if (!address.equals("Not provided")) {
            sb.append("🏠 Address: ").append(address).append("\n\n");
        }

        return sb.toString();
    }

    private void shareProfile() {
        String name = sharedPreferences.getString(KEY_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String phone = sharedPreferences.getString(KEY_PHONE, "");
        String date = sharedPreferences.getString(KEY_DATE, "");
        String address = sharedPreferences.getString(KEY_ADDRESS, "");
        String gender = sharedPreferences.getString(KEY_GENDER, "");

        if (name.isEmpty() && email.isEmpty()) {
            Snackbar.make(requireView(), "No profile data to share", Snackbar.LENGTH_SHORT).show();
            return;
        }

        StringBuilder shareText = new StringBuilder();
        shareText.append("My Profile Information\n\n");

        if (!name.isEmpty()) shareText.append("Name: ").append(name).append("\n");
        if (!email.isEmpty()) shareText.append("Email: ").append(email).append("\n");
        if (!phone.isEmpty()) shareText.append("Phone: ").append(phone).append("\n");
        if (!date.isEmpty()) shareText.append("Date of Birth: ").append(date).append("\n");
        if (!gender.isEmpty()) shareText.append("Gender: ").append(gender).append("\n");
        if (!address.isEmpty()) shareText.append("Address: ").append(address).append("\n");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Profile");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

        startActivity(Intent.createChooser(shareIntent, "Share Profile via"));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to this fragment
        displayProfileData();
    }
}