package com.example.finalproject_androidstudio.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.MainActivity;
import com.example.finalproject_androidstudio.activities.MyUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 1001;
    private static final int CAMERA_REQUEST_CODE = 1002;

    private FirebaseAuth mAuth;
    boolean isBabysitter = false;
    private TextInputEditText fullNameEditText, emailEditText, phoneNumberEditText, socialLinkEditText, salaryEditText, descriptionEditText;
    private TextInputLayout dateEditText, passwordEditText, rePasswordEditText, genderSpinner, regEditTextEmailAddress;
    private AutoCompleteTextView locationSpinner, experienceSpinner, kidsAgeSpinner;
//    private CheckBox babysitterCheckBox;
//    private LinearLayout babysitterForm;
    private Button updateButton, cancelButton, addBabysitter;
    CheckBox babysitterCheckBox;
    ImageView profileImage, idImage;
    LinearLayout form;
    private Button regUploadPhotoBtn, regUploadIDPhotoBtn;

    PhotoType photoType;
    public enum PhotoType {
        PROFILE_PHOTO,
        ID_PHOTO
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        loadUserData();
        setupListeners();
    }

    private void initializeViews(View view) {
        fullNameEditText = view.findViewById(R.id.regEditTextFullName);

        regEditTextEmailAddress = view.findViewById(R.id.regTextEmailAddress);
        regEditTextEmailAddress.setVisibility(View.GONE);

        dateEditText = view.findViewById(R.id.regTextSelectedDate);
        dateEditText.setVisibility(View.GONE);

        passwordEditText = view.findViewById(R.id.regTextPassword);
        rePasswordEditText = view.findViewById(R.id.regTextRePassword);
        passwordEditText.setVisibility(View.GONE);
        rePasswordEditText.setVisibility(View.GONE);

        phoneNumberEditText = view.findViewById(R.id.regPhoneNumberEditText);
        locationSpinner = view.findViewById(R.id.regLocationSpinner);
        genderSpinner = view.findViewById(R.id.regGender);
        genderSpinner.setVisibility(View.GONE);

        babysitterCheckBox = view.findViewById(R.id.regBabysitterCheckBox);
        babysitterCheckBox.setVisibility(View.GONE);

//        babysitterForm = view.findViewById(R.id.regBabysitterForm);
        socialLinkEditText = view.findViewById(R.id.regEditTextSocialLink);
        salaryEditText = view.findViewById(R.id.regEditTextSalary);
        descriptionEditText = view.findViewById(R.id.regEditTextDescription);
        experienceSpinner = view.findViewById(R.id.regSpinnerExperience);
        kidsAgeSpinner = view.findViewById(R.id.regSpinnerKidsAge);

        profileImage = view.findViewById(R.id.regBbsPhoto);
        idImage = view.findViewById(R.id.regBbsIDPhoto);
        regUploadPhotoBtn = view.findViewById(R.id.regUploadPhoto_btn);
        regUploadIDPhotoBtn = view.findViewById(R.id.regUploadIDPhoto_btn);

        updateButton = view.findViewById(R.id.updateButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        form = view.findViewById(R.id.regBabysitterForm);
    }

    private void loadUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Assuming user data has a specific structure. Adjust according to your database structure.
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                        String location = dataSnapshot.child("location").getValue(String.class);

                        fullNameEditText.setText(fullName);
                        phoneNumberEditText.setText(phoneNumber);
                        locationSpinner.setText(location);

                        isBabysitter = dataSnapshot.child("whoAmI").getValue(MyUser.WhoAmI.class) == MyUser.WhoAmI.BABYSITTER ? true : false;
                        Log.d("WhoAmI", String.valueOf(isBabysitter));

                        if (isBabysitter) {
                            String experience = dataSnapshot.child("experience").getValue(String.class);
                            String kidsAgeRange = dataSnapshot.child("kidsAgeRange").getValue(String.class);
                            String socialLink = dataSnapshot.child("socialLink").getValue(String.class);
                            Long salary = dataSnapshot.child("salary").getValue(Long.class);
                            String description = dataSnapshot.child("description").getValue(String.class);

                            DatabaseReference idPhotoUrl = dataSnapshot.getRef().child("idPhotoUrl");
                            idPhotoUrl.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                            String link = dataSnapshot.getValue(String.class);
                                            Picasso.get().load(link).into(idImage);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError){
                                            Toast.makeText(getContext(), "Error Loading Image",Toast.LENGTH_SHORT).show();

                                        }
                                    });

                            DatabaseReference profilePhotoUrl = dataSnapshot.getRef().child("profilePhotoUrl");
                            profilePhotoUrl.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                            String link = dataSnapshot.getValue(String.class);
                                            Picasso.get().load(link).into(profileImage);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError){
                                            Toast.makeText(getContext(), "Error Loading Image",Toast.LENGTH_SHORT).show();

                                        }
                                    });

                            form.setVisibility(View.VISIBLE);
                            experienceSpinner.setText(experience);
                            kidsAgeSpinner.setText(kidsAgeRange);
                            socialLinkEditText.setText(socialLink);
                            salaryEditText.setText(salary != null ? String.valueOf(salary) : null);
                            descriptionEditText.setText(description);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load user data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No user logged in.", Toast.LENGTH_LONG).show();
        }
    }

    private void setupListeners() {
        updateButton.setOnClickListener(v -> updateUserProfile(v));
        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentProfile_to_fragmentMain));

        regUploadPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoType = PhotoType.PROFILE_PHOTO;
                showImageSourceDialog();
            }
        });

        regUploadIDPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoType = PhotoType.ID_PHOTO;
                showImageSourceDialog();
            }
        });
    }

    private void updateUserProfile(View view) {
        String fullName = fullNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String location = locationSpinner.getText().toString();

        // Additional fields if the user is a babysitter
        String experience = null;
        String kidsAgeRange = null;
        String socialLink = null;
        String salary = null;
        String description = null;

        if(isBabysitter){
            experience = experienceSpinner.getText().toString();
            kidsAgeRange = kidsAgeSpinner.getText().toString();
            socialLink = socialLinkEditText.getText().toString().trim();
            salary = salaryEditText.getText().toString().trim();
            description = descriptionEditText.getText().toString().trim();
        }

        // Create a user object or a map to update
        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", fullName);
        userData.put("phoneNumber", phoneNumber);
        userData.put("location", location);

        if (isBabysitter) {
            userData.put("experience", experience);
            userData.put("kidsAgeRange", kidsAgeRange);
            userData.put("socialLink", socialLink);
            userData.put("salary", Double.parseDouble(salary));
            userData.put("description", description);
        }

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(location) ||
                (isBabysitter && (TextUtils.isEmpty(experience) || TextUtils.isEmpty(kidsAgeRange) ||
                        TextUtils.isEmpty(socialLink) || TextUtils.isEmpty(salary) || TextUtils.isEmpty(description)))) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_LONG).show();
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            databaseReference.updateChildren(userData)
                    .addOnSuccessListener(aVoid -> {
                        // Successfully updated the user
                        Toast.makeText(getContext(), "User updated successfully!", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        // Failed to update user
                        Toast.makeText(getContext(), "Failed to update user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

            Navigation.findNavController(view).navigate(R.id.action_fragmentProfile_to_fragmentMain);
        }
    }

    private String sanitizeEmail(String email) {
        if (email == null) return null;
        return email.replace(".", ",");
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        launchGallery();
                        break;
                    case 1:
                        launchCamera();
                        break;
                }
            }
        });
        builder.show();
    }

    private void launchGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getContext(), "No camera app found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                switch (requestCode) {
                    case GALLERY_REQUEST_CODE:
                        // Assuming this is for the profile photo
                        if (photoType == PhotoType.PROFILE_PHOTO) {
                            updateImageInFirebase(selectedImageUri, "profilePhotoUrl");
                        } else if (photoType == PhotoType.ID_PHOTO) {
                            updateImageInFirebase(selectedImageUri, "idPhotoUrl");
                        }
                        break;
                    case CAMERA_REQUEST_CODE:
                        // Handle camera capture
                        break;
                }
            }
        }
    }

    private void updateImageInFirebase(Uri imageUri, String childName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userPath = sanitizeEmail(user.getUid());
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("images")
                    .child(userPath + "/" + childName);

            storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userPath);
                userRef.child(childName).setValue(uri.toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (childName.equals("profilePhotoUrl")) {
                            Picasso.get().load(uri).into(profileImage);
                        } else {
                            Picasso.get().load(uri).into(idImage);
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to update image URL in database!", Toast.LENGTH_SHORT).show();
                    }
                });
            })).addOnFailureListener(e -> Toast.makeText(getContext(), "Upload failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
        }
    }

}