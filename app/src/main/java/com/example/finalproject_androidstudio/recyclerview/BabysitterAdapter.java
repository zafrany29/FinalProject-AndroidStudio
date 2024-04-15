package com.example.finalproject_androidstudio.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.squareup.picasso.Picasso;

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
            fullNameTextView = itemView.findViewById(R.id.smallTextBoxEditText);
            kidsAgeRangeAndAgeTextView = itemView.findViewById(R.id.textEditText);
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

    }

    @Override
    public int getItemCount() {
        return babysitterList.size();
    }

    public void setData(List<Babysitter> babysitters) {
        this.babysitterList = babysitters;
        notifyDataSetChanged();
    }
}