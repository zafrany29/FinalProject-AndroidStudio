package com.example.finalproject_androidstudio.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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

    // Constructor to initialize the list of babysitters
    public BabysitterAdapter(List<Babysitter> babysitterList) {
        this.babysitterList = babysitterList;
    }

    // ViewHolder class to hold references to views within each row
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView;
        TextView kidsAgeRangeAndAgeTextView;
        RatingBar ratingBar;
        ImageView photoImageView;



        public ViewHolder(View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            kidsAgeRangeAndAgeTextView = itemView.findViewById(R.id.detailsTextView);
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
        holder.kidsAgeRangeAndAgeTextView.setText( "ילדים בגיל:"+ babysitter.getKidsAgeRange()+","+babysitter.getLocation());
        holder.ratingBar.setRating((float) babysitter.getRating());
        Picasso.get().load(babysitter.getProfilePhotoUrl()).into(holder.photoImageView);
//        Picasso.get()
//                .load(babysitter.getProfilePhotoUrl())
//                .placeholder(R.drawable.baseline_logout_24) // Placeholder image
//                .error(R.drawable.baseline_logout_24) // Error placeholder image
//                .into(holder.photoImageView);

        holder.itemView.setOnClickListener(v -> {
            showDialog(babysitter, v.getContext()); // Show dialog on item click
        });
    }

    @Override
    public int getItemCount() {
        return babysitterList.size();
    }

    public void setData(List<Babysitter> babysitters) {
        this.babysitterList = babysitters;
        notifyDataSetChanged();
    }

    // Method to show AlertDialog with details
    private void showDialog(Babysitter babysitter, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.babbysitter_user_popup, null);
        builder.setView(dialogView);

        TextView textViewFullName = dialogView.findViewById(R.id.textViewFullName);
        TextView textViewDetails = dialogView.findViewById(R.id.textViewDetails);
        ImageView imageView = dialogView.findViewById(R.id.imageViewDialog);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBarDialog);
        Button rateButton = dialogView.findViewById(R.id.buttonRate);
        Button closeButton = dialogView.findViewById(R.id.buttonClose);

        textViewFullName.setText(babysitter.getFullName());
        textViewDetails.setText("Age Range: " + babysitter.getKidsAgeRange() + ", Location: " + babysitter.getLocation());
        Picasso.get().load(babysitter.getProfilePhotoUrl()).into(imageView);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                updateBabysitterRating(babysitter, rating, context);
                Toast.makeText(context, "Sent rating!", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        rateButton.setOnClickListener(v -> ratingBar.setVisibility(View.VISIBLE));
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateBabysitterRating(Babysitter babysitter, float newRating, Context context) {
        String emailKey = sanitizeEmail(babysitter.getEmail());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(emailKey);
        System.out.println("Firebase Path: " + ref.toString());  // Check if this path is correct

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Babysitter b = babysitter;
                if (b == null) {
                    // Babysitter might not exist if the key is wrong or data has been deleted
                    return Transaction.abort();  // Stops the transaction
                }

                // Update the ratings by recalculating the average
                int newReviewCount = b.getRatingCount() + 1;
                double oldRatingsTotal = b.getRating() * b.getRatingCount();
                double newAverageRating = (oldRatingsTotal + newRating) / newReviewCount;

                // Set updated values
                b.setRating(newAverageRating);
                b.setRatingCount(newReviewCount);
                mutableData.setValue(b);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (committed) {
                    Toast.makeText(context, "Rating updated!", Toast.LENGTH_SHORT).show();
                } else {
                    if (databaseError != null) {
                        // There was an error during the transaction.
                        Toast.makeText(context, "Failed to update rating: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        // Transaction was aborted or didn't result in a commit but without a Firebase error.
                        Toast.makeText(context, "Update was not committed due to a transaction abort or no data change.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String sanitizeEmail(String email) {
        if (email != null) {
            return email.replace(".", ",");
        }
        return email;
    }

}