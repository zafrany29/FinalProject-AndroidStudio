package com.example.finalproject_androidstudio.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BabysitterAdapter extends RecyclerView.Adapter<BabysitterAdapter.ViewHolder> {

    private List<Babysitter> babysitterList;
    private List<Babysitter> filteredBabysitterList; // Filtered list based on location


    // Constructor to initialize the list of babysitters
    public BabysitterAdapter(List<Babysitter> babysitterList) {
        this.babysitterList = babysitterList;
        this.filteredBabysitterList = new ArrayList<>(babysitterList); // Initially set filtered list to full list

    }

    // ViewHolder class to hold references to views within each row
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView;
        TextView ageDetailsTextView, experienceDetailsTextView, rangeDetailsTextView, salaryDetailsTextView;
        RatingBar ratingBar;
        ImageView photoImageView;



        public ViewHolder(View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            ageDetailsTextView = itemView.findViewById(R.id.ageDetailsTextView);
            experienceDetailsTextView = itemView.findViewById(R.id.experienceDetailsTextView);
            rangeDetailsTextView = itemView.findViewById(R.id.rangeDetailsTextView);
            salaryDetailsTextView = itemView.findViewById(R.id.salaryDetailsTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            photoImageView = itemView.findViewById(R.id.photoImageView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Babysitter babysitter = babysitterList.get(position);
        holder.fullNameTextView.setText(babysitter.getFullName());
        holder.ageDetailsTextView.setText("גיל: " + String.valueOf(babysitter.getAge()));
        holder.experienceDetailsTextView.setText("שנות ניסיון: " + String.valueOf(babysitter.getExperience()));
        holder.rangeDetailsTextView.setText("טווח גילאים: " + String.valueOf(babysitter.getKidsAgeRange()));
        holder.salaryDetailsTextView.setText("מחיר שעתי מצופה: " + String.valueOf(babysitter.getSalary()) + " ש\"ח");
        holder.ratingBar.setRating((float) babysitter.getRating());
        Picasso.get().load(babysitter.getProfilePhotoUrl()).into(holder.photoImageView);

        holder.itemView.setOnClickListener(v -> {
            showDialog(babysitter, v.getContext()); // Show dialog on item click
        });
    }

    @Override
    public int getItemCount() {
        return filteredBabysitterList.size();
    }

    // Method to set full list of babysitters and update filtered list
    public void setData(List<Babysitter> babysitters) {
        this.babysitterList = babysitters;
        filterData("");
    }
    public void filterData(String location) {
        filteredBabysitterList.clear();
        if (location.isEmpty() || location.equals("All Locations")) {
            // No filter or All Locations selected, show full list
            filteredBabysitterList.addAll(babysitterList);
        } else {
            // Filter by location (case-insensitive comparison)
            for (Babysitter babysitter : babysitterList) {
                if (babysitter.getLocation().equalsIgnoreCase(location)) {
                    filteredBabysitterList.add(babysitter);
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter of data change
    }

    // Method to show AlertDialog with details
    private void showDialog(Babysitter babysitter, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.babbysitter_user_popup, null);
        builder.setView(dialogView);

        TextView textViewFullName = dialogView.findViewById(R.id.textViewFullName);
        TextView textViewDetails = dialogView.findViewById(R.id.textViewDetails);
        TextView phoneTextView = dialogView.findViewById(R.id.phoneTextView);
        TextView emailTextView = dialogView.findViewById(R.id.emailTextView);
        ImageView imageView = dialogView.findViewById(R.id.imageViewDialog);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBarDialog);
        Button rateButton = dialogView.findViewById(R.id.buttonRate);
        ScrollView scrollView = dialogView.findViewById(R.id.scrollView);
        TextView scroll = dialogView.findViewById(R.id.scrolling_textview);
        Button closeButton = dialogView.findViewById(R.id.buttonClose);

        textViewFullName.setText(babysitter.getFullName());
        textViewDetails.setText("גיל: " + babysitter.getAge() + ", מחוז: " + babysitter.getLocation());
        phoneTextView.append(babysitter.getPhoneNumber());
        emailTextView.append(babysitter.getEmail());
        scroll.setText(babysitter.getDescription());
        scrollView.scrollTo(0, 0);


        Picasso.get().load(babysitter.getProfilePhotoUrl()).into(imageView);

        AlertDialog dialog = builder.create();

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                updateBabysitterRating(babysitter, rating, context, dialog);
                Toast.makeText(context, "Sent rating!", Toast.LENGTH_SHORT).show();
            }
        });

        rateButton.setOnClickListener(v -> ratingBar.setVisibility(View.VISIBLE));
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateBabysitterRating(Babysitter babysitter, float newRating, Context context, AlertDialog dialog) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        // Query users by email
        Query query = ref.orderByChild("email").equalTo(babysitter.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Update each found babysitter
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Babysitter foundBabysitter = snapshot.getValue(Babysitter.class);
                        if (foundBabysitter != null) {
                            int newReviewCount = foundBabysitter.getRatingCount() + 1;
                            double oldRatingsTotal = foundBabysitter.getRating() * foundBabysitter.getRatingCount();
                            double newAverageRating = (oldRatingsTotal + newRating) / newReviewCount;

                            foundBabysitter.setRating(newAverageRating);
                            foundBabysitter.setRatingCount(newReviewCount);

                            // Update the database with the new rating
                            snapshot.getRef().setValue(foundBabysitter)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Rating updated successfully!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to update rating.", Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    Toast.makeText(context, "No matching babysitter found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}