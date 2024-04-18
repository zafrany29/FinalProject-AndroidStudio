package com.example.finalproject_androidstudio.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.example.finalproject_androidstudio.fragments.BabysitterDetailDialogFragment;
import com.squareup.picasso.Picasso;


import java.util.List;

public class BabysitterAdminAdapter extends RecyclerView.Adapter<BabysitterAdminAdapter.BabysitterViewHolder> {
    private List<Babysitter> babysitterList;
    private List<String> babysitterIds; // This list will store the Firebase UIDs
    private Context context;

    public BabysitterAdminAdapter(Context context, List<Babysitter> babysitterList, List<String> babysitterIds) {
        this.context = context;
        this.babysitterList = babysitterList;
        this.babysitterIds = babysitterIds;
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
        String babysitterId = babysitterIds.get(position); // Get the UID from the list
        holder.itemView.setOnClickListener(v -> {
            BabysitterDetailDialogFragment dialogFragment = BabysitterDetailDialogFragment.newInstance(babysitterId);
            dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "detail");
        });

        holder.bind(babysitter);
    }

    @Override
    public int getItemCount() {
        return babysitterList.size();
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
            nameTextView.setText(babysitter.getFullName());
            emailTextView.setText(babysitter.getEmail());
            approvedCheckBox.setChecked(babysitter.isVerified());
            // Load image using Picasso
            if (babysitter.getProfilePhotoUrl() != null && !babysitter.getProfilePhotoUrl().isEmpty()) {
                Picasso.get().load(babysitter.getProfilePhotoUrl()).into(babysitterImageView);
            } else {
                // If there's no image URL provided, you can set a placeholder image
                babysitterImageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}
