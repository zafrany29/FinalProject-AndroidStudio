package com.example.finalproject_androidstudio.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;

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
        TextView kidsAgeRangeAndExperienceTextView;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.smallTextBoxEditText);
            kidsAgeRangeAndExperienceTextView = itemView.findViewById(R.id.textEditText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
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
        holder.kidsAgeRangeAndExperienceTextView.setText(babysitter.getKidsAgeRange() + ", " + babysitter.getExperience());
        holder.ratingBar.setRating((float) babysitter.getRating());
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