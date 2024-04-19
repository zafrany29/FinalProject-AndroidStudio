package com.example.finalproject_androidstudio.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.example.finalproject_androidstudio.activities.MainActivity;
import com.example.finalproject_androidstudio.activities.MyUser;
import com.example.finalproject_androidstudio.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class FragmentRegister extends Fragment {

    private View rootView;
    private Button registerBtn;
    private Button regUploadPhotoBtn;
    private Button regUploadIDPhotoBtn;
    private ImageView regBbsPhoto;
    private ImageView regBbsIDPhoto;
    private EditText regTextFullName;
    private EditText regTextEmailAddress;
    private EditText regTextSelectedDate;
    private EditText regTextPassword;
    private EditText regTextRePassword;
    private EditText regPhoneNumber;
    private AutoCompleteTextView regLocation;
    private AutoCompleteTextView regGender;
    private AutoCompleteTextView regSpinnerExperience;
    private AutoCompleteTextView regSpinnerKidsAge;
    private EditText regEditTextSocialLink;
    private EditText regEditTextSalary;
    private EditText regEditTextDescription;
    private CheckBox regBabysitterCheckBox;
    private LinearLayout regBabysitterForm;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Uri profilePhoto;
    private Uri idPhoto;
    private PhotoType photoType;
    private boolean isBabysitter;
    public enum PhotoType {
        PROFILE_PHOTO,
        ID_PHOTO
    }
    private static final int GALLERY_REQUEST_CODE = 1001;
    private static final int CAMERA_REQUEST_CODE = 1002;

    public FragmentRegister() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        initViews();

        initSpinners();

        initListeners();

        regBabysitterForm.setVisibility(regBabysitterCheckBox.isChecked() ? View.VISIBLE : View.GONE);

        return rootView;
    }

    private void initListeners(){

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

        regBabysitterCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle visibility of the babysitter form
                regBabysitterForm.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Register the user with Firebase Authentication
                registerUser();
            }
        });
    }

    private void initViews() {
        registerBtn = rootView.findViewById(R.id.register_btnn);
        regTextFullName = rootView.findViewById(R.id.regEditTextFullName);
        regTextEmailAddress = rootView.findViewById(R.id.regEditTextEmailAddress);
        regTextSelectedDate = rootView.findViewById(R.id.regEditTextSelectedDate);
        regTextPassword = rootView.findViewById(R.id.regPasswordEditText);
        regTextRePassword = rootView.findViewById(R.id.regRePasswordEditText);
        regPhoneNumber = rootView.findViewById(R.id.regPhoneNumberEditText);
        regLocation = rootView.findViewById(R.id.regLocationSpinner);
        regGender = rootView.findViewById(R.id.regGenderSpinner);
        regSpinnerExperience = rootView.findViewById(R.id.regSpinnerExperience);
        regSpinnerKidsAge = rootView.findViewById(R.id.regSpinnerKidsAge);
        regEditTextSocialLink = rootView.findViewById(R.id.regEditTextSocialLink);
        regEditTextSalary = rootView.findViewById(R.id.regEditTextSalary);
        regEditTextDescription = rootView.findViewById(R.id.regEditTextDescription);
        regBabysitterCheckBox = rootView.findViewById(R.id.regBabysitterCheckBox);
        regBabysitterForm = rootView.findViewById(R.id.regBabysitterForm);

        regUploadPhotoBtn = rootView.findViewById(R.id.regUploadPhoto_btn);
        regUploadIDPhotoBtn = rootView.findViewById(R.id.regUploadIDPhoto_btn);
        regBbsPhoto = rootView.findViewById(R.id.regBbsPhoto);
        regBbsIDPhoto = rootView.findViewById(R.id.regBbsIDPhoto);
    }

    private void initSpinners() {
        // gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regGender.setAdapter(genderAdapter);

        // location spinner
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.location_array, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regLocation.setAdapter(locationAdapter);

        // kids age spinner
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.ageRange, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regSpinnerKidsAge.setAdapter(ageAdapter);

        // experience spinner
        ArrayAdapter<CharSequence> experienceAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.experience, android.R.layout.simple_spinner_item);
        experienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regSpinnerExperience.setAdapter(experienceAdapter);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                // Handle image selected from gallery
                Uri selectedImageUri = data.getData();
                handleImage(selectedImageUri);
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null && data.getExtras() != null) {
                // Handle image captured from camera
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                Uri imageUri = getImageUri(imageBitmap, this.getContext());
                handleImage(imageUri);
            }
        }
    }
    private Uri getImageUri(Bitmap bitmap, Context context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    private void handleImage(Uri imageUri) {
        // Set the image to the appropriate ImageView based on the photo type
        if (imageUri != null) {
            switch (photoType) {
                case PROFILE_PHOTO:
                    regBbsPhoto.setImageURI(imageUri);
                    profilePhoto = imageUri;
                    break;
                case ID_PHOTO:
                    regBbsIDPhoto.setImageURI(imageUri);
                    idPhoto = imageUri;
                    break;
            }
        } else {
            Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_LONG).show();
        }
        photoType = null;
    }

    // Back end

    private Object validateUserForm() {
        // Get user data from UI fields
        String fullName = regTextFullName.getText().toString().trim();
        String email = regTextEmailAddress.getText().toString().trim();
        String password = regTextPassword.getText().toString().trim();
        String reEnteredPassword = regTextRePassword.getText().toString().trim();
        String phoneNumber = regPhoneNumber.getText().toString().trim();
        String location = regLocation.getText().toString().trim();
        String experience = regSpinnerExperience.getText().toString().trim();
        String kidsAgeRange = regSpinnerKidsAge.getText().toString().trim();
        String socialLink = regEditTextSocialLink.getText().toString().trim();
        String salary = regEditTextSalary.getText().toString().trim();
        String description = regEditTextDescription.getText().toString().trim();

        // Check if the checkbox is checked to determine user type
        isBabysitter = regBabysitterCheckBox.isChecked();

        // Get image URLs as strings
        String profilePhotoUrl = getImageUrlFromImageView(regBbsPhoto);
        String idPhotoUrl = getImageUrlFromImageView(regBbsIDPhoto);

        // Validate the data
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(getContext(), "Please enter your full name", Toast.LENGTH_LONG).show();
            return null;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please enter your email address", Toast.LENGTH_LONG).show();
            return null;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_LONG).show();
            return null;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_LONG).show();
            return null;
        }

        if (!TextUtils.equals(password, reEnteredPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            return null;
        }
        if(isBabysitter)
        {
            if(salary == null || salary.isEmpty())
            {
                try {
                    double parsedSalary = Double.parseDouble(salary);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getContext(), "Please enter Salary", Toast.LENGTH_LONG).show();
                return null;
            }
            if(profilePhotoUrl == null || profilePhotoUrl.toString().isEmpty())
            {
                Toast.makeText(getContext(), "Please Select Profile Photo", Toast.LENGTH_LONG).show();
            }
            if(idPhoto == null || idPhoto.toString().isEmpty())
            {
                Toast.makeText(getContext(), "Please Select ID Photo", Toast.LENGTH_LONG).show();
            }
        }

        if (isBabysitter) {
            // If the checkbox is checked, create a Babysitter object
            return new Babysitter(fullName, email, phoneNumber, location, description, null,
                    profilePhotoUrl, idPhotoUrl, socialLink, experience, null, kidsAgeRange,
                    Double.parseDouble(salary), 0, false);
        } else {
            // If the checkbox is unchecked, create a regular User object
            return new MyUser(fullName, email, phoneNumber, location, null, MyUser.WhoAmI.USER);
        }
    }

    private String getImageUrlFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();

            // Convert Bitmap to URL string and return
            return bitmapToString(bitmap);
        } else {

            return null;
        }
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void registerUser() {
        // Validate user form data
        Object newUser = validateUserForm();
        String email = regTextEmailAddress.getText().toString().trim();
        String password = regTextPassword.getText().toString().trim();

        if (newUser != null) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                // Add user data to Firebase Realtime Database
                                if (user != null) {
                                    uploadUserDataToRealtimeDatabase(newUser);
                                } else {
                                    Toast.makeText(getContext(), "Failed to get user information", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getContext(), "Authentication failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void uploadImage(Uri imageUri, String imageName, String userId) {
        if (imageUri == null) {
            Toast.makeText(getContext(), "Image Uri is null, can't upload", Toast.LENGTH_SHORT).show();
            return;
        }
        final StorageReference ref = mStorageRef.child("images/" + UUID.randomUUID().toString() + "_" + imageName);
        UploadTask uploadTask = ref.putFile(imageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String photoUrl = uri.toString();
                        // Save photo URL depending on the type of the image
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                        if (imageName.equals("profile")) {
                            userRef.child("profilePhotoUrl").setValue(photoUrl);
                        } else {
                            userRef.child("idPhotoUrl").setValue(photoUrl);
                        }
                        Toast.makeText(getContext(), "Image uploaded successfully: " + photoUrl, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadUserDataToRealtimeDatabase(Object userData) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.setValue(userData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_LONG).show();



                        if(isBabysitter)
                        {
                            // Upload images and save URLs for babysitter
                            uploadImage(profilePhoto, "profile", userId);
                            uploadImage(idPhoto, "ID", userId);
                        }

                        Navigation.findNavController(requireView()).navigate(R.id.action_fragmentRegister_to_fragmentMain);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to register user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            Toast.makeText(getContext(), "No authenticated user available.", Toast.LENGTH_LONG).show();
        }
    }
}
