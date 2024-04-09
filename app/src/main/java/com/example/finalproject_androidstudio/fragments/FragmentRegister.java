package com.example.finalproject_androidstudio.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.example.finalproject_androidstudio.activities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentRegister extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private CheckBox regBabysitterCheckBox;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private LinearLayout regBabysitterForm;
    private Button registerButton;
    private Button returnButton;
    private View rootView;
    private EditText selectedDateEditText;
    private TextInputLayout selectedDateInputLayout;

    private static final int PHOTO_PICK_REQUEST_CODE = 1;
    private static final int ID_PHOTO_PICK_REQUEST_CODE = 2;

    private ImageView regBbsPhoto;
    private ImageView regBbsIDPhoto;
    private Button regUploadPhotoButton;
    private Button regUploadIDPhotoButton;



    public FragmentRegister() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register, container, false);

        // Find the button
        selectedDateInputLayout = rootView.findViewById(R.id.regTextSelectedDate);
        selectedDateEditText = rootView.findViewById(R.id.regEditTextSelectedDate);

        // Set OnClickListener to show DatePickerDialog
        selectedDateInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        rootView = view;

        editTextEmail = view.findViewById(R.id.regEditTextEmailAddress);
        editTextPassword = view.findViewById(R.id.regPasswordEditText);
        regBabysitterCheckBox = view.findViewById(R.id.regBabysitterCheckBox);
        regBabysitterForm = view.findViewById(R.id.regBabysitterForm);
        registerButton = view.findViewById(R.id.register_btn);
        returnButton = view.findViewById(R.id.return_btn);

        initPhotoButtons(view);
        initSpinners(view);

        regBabysitterCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                regBabysitterForm.setVisibility(View.VISIBLE);
            } else {
                regBabysitterForm.setVisibility(View.GONE);
            }
        });


        // Set click listeners
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back
                getActivity().onBackPressed();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                regBabysitterForm.setVisibility(View.VISIBLE);
            } else {
                // Permission denied, show a toast message
                Toast.makeText(getActivity(), "הרשאות למצלמה ולגלריה לא ניתנו, לא ניתן לפתוח פרופיל נותן שירות", Toast.LENGTH_LONG).show();
                regBabysitterForm.setVisibility(View.GONE);
            }
        }
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private  void initPhotoButtons(View view)
    {
        regUploadPhotoButton = view.findViewById(R.id.regUploadPhoto_btn);
        regUploadIDPhotoButton = view.findViewById(R.id.regUploadIDPhoto_btn);
        regBbsPhoto = rootView.findViewById(R.id.regBbsPhoto);
        regBbsIDPhoto = rootView.findViewById(R.id.regBbsIDPhoto);


        // Set OnClickListener for Upload Photo button
        regUploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prompt user to select a photo from gallery
                openGalleryForPhoto();
            }
        });

        // Set OnClickListener for Upload ID Photo button
        regUploadIDPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prompt user to select an ID photo from gallery
                openGalleryForIDPhoto();
            }
        });
    }
    private void initSpinners(View view)
    {
        // Set adapter for gender AutoCompleteTextView
        AutoCompleteTextView regGenderSpinner = view.findViewById(R.id.regGenderSpinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.gender_array, android.R.layout.simple_dropdown_item_1line);
        regGenderSpinner.setAdapter(genderAdapter);

        // Set adapter for location AutoCompleteTextView
        AutoCompleteTextView regLocationSpinner = view.findViewById(R.id.regLocationSpinner);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.location_array, android.R.layout.simple_dropdown_item_1line);
        regLocationSpinner.setAdapter(locationAdapter);

        // Set adapter for age range AutoCompleteTextView (assuming you have one)
        AutoCompleteTextView regAgeRangeSpinner = view.findViewById(R.id.regSpinnerKidsAge);
        ArrayAdapter<CharSequence> ageRangeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.ageRange, android.R.layout.simple_dropdown_item_1line);
        regAgeRangeSpinner.setAdapter(ageRangeAdapter);

        // Set adapter for experience AutoCompleteTextView (assuming you have one)
        AutoCompleteTextView regExperienceSpinner = view.findViewById(R.id.regSpinnerExperience);
        ArrayAdapter<CharSequence> experienceAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.experience, android.R.layout.simple_dropdown_item_1line);
        regExperienceSpinner.setAdapter(experienceAdapter);
    }
    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            createAppropriateUser(user);
                        } else {
                            // Registration failed
                            Toast.makeText(getContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createAppropriateUser(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            boolean isBabysitter = regBabysitterCheckBox.isChecked();
            if (isBabysitter) {
                Babysitter babysitter = populateBabysitterFrom();
                if (babysitter != null) {
                    saveBabysitterDataToRealtimeDatabase(firebaseUser, babysitter);
                } else {
                    // Show a message to the user indicating missing or invalid fields
                    Toast.makeText(getContext(), "Please fill in all babysitter fields", Toast.LENGTH_SHORT).show();
                }
            } else {
                User user = populateUserFromForm();
                if (user != null) {
                    saveUserDataToRealtimeDatabase(firebaseUser, user);
                } else {
                    // Show a message to the user indicating missing or invalid fields
                    Toast.makeText(getContext(), "Please fill in all user fields", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private User populateUserFromForm() {
        EditText fullNameEditText = rootView.findViewById(R.id.regEditTextFullName);
        EditText emailEditText = rootView.findViewById(R.id.regEditTextEmailAddress);
        EditText phoneNumberEditText = rootView.findViewById(R.id.regPhoneNumberEditText);
        EditText locationEditText = rootView.findViewById(R.id.regLocationSpinner);
        EditText passwordEditText = rootView.findViewById(R.id.regPasswordEditText);
        EditText rePasswordEditText = rootView.findViewById(R.id.regRePasswordEditText);

        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();

        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || location.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!rePassword.equals(password)) {
            Toast.makeText(getContext(), "Password must match", Toast.LENGTH_SHORT).show();
            passwordEditText.setText("");
            rePasswordEditText.setText("");
            return null;
        }

        if (password.length() < 8) {
            Toast.makeText(getContext(), "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return null;
        }

        return new User(fullName, email, phoneNumber, location, null, User.WhoAmI.USER);
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private Babysitter populateBabysitterFrom() {
        // Get references to all the form fields
        EditText fullNameEditText = rootView.findViewById(R.id.regEditTextFullName);
        EditText emailEditText = rootView.findViewById(R.id.regEditTextEmailAddress);
        EditText phoneNumberEditText = rootView.findViewById(R.id.regPhoneNumberEditText);
        EditText locationEditText = rootView.findViewById(R.id.regLocationSpinner);
        EditText descriptionEditText = rootView.findViewById(R.id.regEditTextDescription);
        EditText selectedDateEditText = rootView.findViewById(R.id.regEditTextSelectedDate);
        EditText socialLinkEditText = rootView.findViewById(R.id.regEditTextSocialLink);
        EditText experienceEditText = rootView.findViewById(R.id.regSpinnerExperience);
        EditText kidsAgeEditText = rootView.findViewById(R.id.regSpinnerKidsAge);
        EditText salaryEditText = rootView.findViewById(R.id.regEditTextSalary);

        // Get the text from all the form fields
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String selectedDate = selectedDateEditText.getText().toString().trim();
        String socialLink = socialLinkEditText.getText().toString().trim();
        int experience = Integer.parseInt(experienceEditText.getText().toString().trim());
        String kidsAge = kidsAgeEditText.getText().toString().trim();
        double salary = Double.parseDouble(salaryEditText.getText().toString().trim());

        // Create a List<String> to hold the calendar data (assuming you have a way to get it)
        List<String> calendar = new ArrayList<>(); // Populate this list with calendar data

        // Create a Babysitter object and populate it with the retrieved data
        Babysitter babysitter = new Babysitter();
        babysitter.setFullName(fullName);
        babysitter.setEmail(email);
        babysitter.setPhoneNumber(phoneNumber);
        babysitter.setLocation(location);
        babysitter.setDescription(description);
        babysitter.setCalendar(calendar);
        babysitter.setSocialLink(socialLink);
        babysitter.setExperience(experience);
        babysitter.setKidsAgeRange(kidsAge);
        babysitter.setSalary(salary);

        // Set other specific fields of Babysitter here

        return babysitter;
    }


    private void saveBabysitterDataToRealtimeDatabase(FirebaseUser firebaseUser, Babysitter babysitter) {
        // Get a reference to the Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference babysittersRef = database.getReference("babysitters");

        // Get the UID of the current user
        String userId = firebaseUser.getUid();

        // Create a reference to the user's data using the UID
        DatabaseReference babysitterRef = babysittersRef.child(userId);

        // Write the babysitter data to the database
        babysitterRef.setValue(babysitter)
                .addOnSuccessListener(aVoid -> {
                    // Data saved successfully
                    Toast.makeText(getContext(), "Babysitter data added to Realtime Database successfully!", Toast.LENGTH_SHORT).show();
                    // Navigate to the main fragment
                    Navigation.findNavController(rootView).navigate(R.id.action_fragmentRegister_to_fragmentMain);
                })
                .addOnFailureListener(e -> {
                    // Error occurred while saving data
                    Toast.makeText(getContext(), "Error adding babysitter data to Realtime Database", Toast.LENGTH_SHORT).show();
                });
    }


    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and set listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the selected date to the button text
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                selectedDateEditText.setText(selectedDate);
            }
        }, year, month, day);

        // Show the dialog
        datePickerDialog.show();
    }

    // Assuming this method is inside an activity or fragment
    // Assuming this method is inside an activity or fragment
    // Assuming this method is inside a fragment
    private void saveUserDataToRealtimeDatabase(FirebaseUser firebaseUser, User user) {
        // Find the NavController associated with the activity
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

        // Write the user data to the database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        String userId = firebaseUser.getUid();
        DatabaseReference userRef = usersRef.child(userId);
        userRef.setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // Data saved successfully
                    Toast.makeText(getContext(), "User data added to Realtime Database successfully!", Toast.LENGTH_SHORT).show();
                    // Navigate to the main fragment
                    navController.navigate(R.id.action_fragmentRegister_to_fragmentMain);
                })
                .addOnFailureListener(e -> {
                    // Error occurred while saving data
                    String errorMessage = "Error adding user data to Realtime Database: " + e.getMessage();
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", errorMessage); // Log the error for debugging
                });
    }

    private void openGalleryForPhoto() {
        // Intent to pick an image from gallery
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_PICK_REQUEST_CODE);
    }

    private void openGalleryForIDPhoto() {
        // Intent to pick an ID image from gallery
        Intent idPhotoPickerIntent = new Intent(Intent.ACTION_PICK);
        idPhotoPickerIntent.setType("image/*");
        startActivityForResult(idPhotoPickerIntent, ID_PHOTO_PICK_REQUEST_CODE);
    }

    // Handle the result after the user selects an image from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PHOTO_PICK_REQUEST_CODE && data != null) {
                // Update the ImageView with the selected photo
                Uri imageUri = data.getData();
                regBbsPhoto.setImageURI(imageUri);
            } else if (requestCode == ID_PHOTO_PICK_REQUEST_CODE && data != null) {
                // Update the ImageView with the selected ID photo
                Uri imageUri = data.getData();
                regBbsIDPhoto.setImageURI(imageUri);
            }
        }
    }
}
