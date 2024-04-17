package com.example.finalproject_androidstudio.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;

import java.util.List;

public class BabysitterAdminAdapter extends RecyclerView.Adapter<BabysitterAdminAdapter.BabysitterViewHolder> {

    private List<Babysitter> babysitterList;

    public BabysitterAdminAdapter(List<Babysitter> babysitterList) {
        this.babysitterList = babysitterList;
    }

    @NonNull
    @Override
    public BabysitterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.babbysitter_admin_recycleview, parent, false);
        return new BabysitterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BabysitterViewHolder holder, int position) {
        Babysitter babysitter = babysitterList.get(position);
        holder.bind(babysitter);
    }

    @Override
    public int getItemCount() {
        return babysitterList.size();
    }

    public void setData(List<Babysitter> babysitterList) {
        this.babysitterList = babysitterList;
        notifyDataSetChanged();
    }

    public static class BabysitterViewHolder extends RecyclerView.ViewHolder {

        private ImageView babysitterImageView;
        private TextView nameTextView;
        private TextView emailTextView;
        private CheckBox approvedCheckBox;

        public BabysitterViewHolder(@NonNull View itemView) {
            super(itemView);
            babysitterImageView = itemView.findViewById(R.id.babbysitter_img);
            nameTextView = itemView.findViewById(R.id.babbysitter_name);
            emailTextView = itemView.findViewById(R.id.babbysitter_email);
            approvedCheckBox = itemView.findViewById(R.id.approved_cb);
        }

        public void bind(Babysitter babysitter) {
            // Bind data to views
            nameTextView.setText(babysitter.getFullName());
            emailTextView.setText(babysitter.getEmail());
            // Set other properties as needed
        }
    }
}
