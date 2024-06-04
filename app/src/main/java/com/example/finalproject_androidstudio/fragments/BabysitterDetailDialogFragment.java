package com.example.finalproject_androidstudio.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Admin;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BabysitterDetailDialogFragment extends DialogFragment {

    private ImageView profileImageView, idImageView, closeImageView;
    private TextView descriptionTextView;
    private Button verifyButton;
    private DatabaseReference databaseReference;
    private GridView detailsGridView;
    private Button downloadButton;
    private String babysitterId;
    private Babysitter currentBabysitter;

    private String adminId;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;


    public static BabysitterDetailDialogFragment newInstance(String babysitterId, String adminId) {
        BabysitterDetailDialogFragment fragment = new BabysitterDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("BABYSITTER_ID", babysitterId);
        args.putString("ADMIN_ID", adminId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.babbysitter_admin_popup, container, false);

        profileImageView = view.findViewById(R.id.admin_page_profilePhoto);
        idImageView = view.findViewById(R.id.admin_page_idPhoto);
        descriptionTextView = view.findViewById(R.id.scrolling_textview_description);
        verifyButton = view.findViewById(R.id.admin_page_verify_btn);
        detailsGridView= view.findViewById(R.id.user_grid_view);

        this.babysitterId = getArguments().getString("BABYSITTER_ID", "");
        this.adminId = getArguments().getString("ADMIN_ID", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("babysitters").child(babysitterId);
        closeImageView = view.findViewById(R.id.admin_popup_close_btn);

        // Loads the babysitter details here
        loadBabysitterDetails();

        verifyButton.setOnClickListener(v -> updateBabysitterVerification(true));
        closeImageView.setOnClickListener(v -> dismiss());
        downloadButton = view.findViewById(R.id.admin_page_downloadImages_btn);
        downloadButton.setOnClickListener(v -> downloadImages());

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadImages();
            } else {
                Toast.makeText(getContext(), "Permission denied to write to your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadBabysitterDetails() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(this.babysitterId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentBabysitter = dataSnapshot.getValue(Babysitter.class);
                if (currentBabysitter != null) {
                    Picasso.get().load(currentBabysitter.getProfilePhotoUrl()).into(profileImageView);
                    Picasso.get().load(currentBabysitter.getIdPhotoUrl()).into(idImageView);
                    descriptionTextView.setText(currentBabysitter.getDescription());

                    List<String> details = new ArrayList<>();
                    details.add("שם מלא:" + currentBabysitter.getFullName());
                    details.add("מייל:" + currentBabysitter.getEmail());
                    details.add("גיל: " + currentBabysitter.getAge());
                    details.add("ניסיון: " + currentBabysitter.getExperience());
                    details.add("טווח גיל: " + currentBabysitter.getKidsAgeRange());
                    details.add("מיקום: " + currentBabysitter.getLocation());
                    details.add("שכר: ₪" + currentBabysitter.getSalary());
                    details.add("לינק ציבורי: " + currentBabysitter.getSocialLink());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, details);
                    detailsGridView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load details. Babysitter data is null.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load babysitter details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBabysitterVerification(boolean isVerified) {
        databaseReference.child("verified").setValue(isVerified)
                .addOnCompleteListener(task -> {
                    if (isAdded()) {
                        if (task.isSuccessful()) {
                            updateAdminLists(babysitterId, isVerified);

                            Toast.makeText(getContext(), "Status updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to update status", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dismiss();
    }
    private void updateAdminLists(String babysitterId, boolean isVerified) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("admins").child("YOUR_ADMIN_ID"); // Replace with actual admin ID

        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                if (admin != null) {
                    if (isVerified) {
                        admin.getVerifiedBabysitters().add(babysitterId);
                    } else {
                        admin.getBlacklistedBabysitters().add(babysitterId);
                    }
                    adminRef.setValue(admin);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to update admin lists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadImages() {
        // Gets the URL`s of profile and id photo`s
        String profileImageUrl = currentBabysitter.getProfilePhotoUrl();
        String idImageUrl = currentBabysitter.getIdPhotoUrl();

        downloadImage(profileImageUrl, "profile_image.jpg");
        downloadImage(idImageUrl, "id_image.jpg");
    }

    private void downloadImage(String imageUrl, String fileName) {
        if (imageUrl != null && !imageUrl.isEmpty()) {

            // Create DownloadManager Request
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl))
                    .setTitle("Downloading " + fileName)
                    .setDescription("Downloading " + fileName)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);

            // Set the local destination for the downloaded file to a path within the application's external files directory
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            } else {
                File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
                request.setDestinationUri(Uri.fromFile(file));
            }

            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);

            Toast.makeText(getContext(), "Downloading image:" + fileName, Toast.LENGTH_SHORT).show();
        }
    }


}
