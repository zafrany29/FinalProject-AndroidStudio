package com.example.finalproject_androidstudio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject_androidstudio.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BabysitterDetailDialogFragment extends DialogFragment {

    private ImageView profileImageView;
    private TextView nameTextView, emailTextView, descriptionTextView;
    private Button verifyButton, unverifyButton;
    private DatabaseReference databaseReference;

    public static BabysitterDetailDialogFragment newInstance(String babysitterId) {
        BabysitterDetailDialogFragment fragment = new BabysitterDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("BABYSITTER_ID", babysitterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.babbysitter_admin_popup, container, false);

        profileImageView = view.findViewById(R.id.profile_photo_popup);
        nameTextView = view.findViewById(R.id.babbysitter_name); // Make sure you have these IDs in your layout
        emailTextView = view.findViewById(R.id.babbysitter_email);
        descriptionTextView = view.findViewById(R.id.scrolling_textview);
        verifyButton = view.findViewById(R.id.btnVerify);
        unverifyButton = view.findViewById(R.id.btnUnverify);

        String babysitterId = getArguments().getString("BABYSITTER_ID", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("babysitters").child(babysitterId);

        // Load babysitter details here
        loadBabysitterDetails(babysitterId);

        verifyButton.setOnClickListener(v -> updateBabysitterVerification(babysitterId, true));
        unverifyButton.setOnClickListener(v -> updateBabysitterVerification(babysitterId, false));

        return view;
    }

    private void loadBabysitterDetails(String babysitterId) {
        // Implement Firebase database fetching logic here
    }

    private void updateBabysitterVerification(String babysitterId, boolean isVerified) {
        databaseReference.child("is_verified").setValue(isVerified)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Status updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update status", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
