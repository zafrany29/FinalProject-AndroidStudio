package com.example.finalproject_androidstudio.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_androidstudio.activities.MyUser;

import java.util.ArrayList;

public class AdminRVAdapter extends RecyclerView.Adapter<AdminRVAdapter.AdminViewHolder>{

    Context context;
    ArrayList<MyUser> myUserList;

    public AdminRVAdapter() {
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static  class AdminViewHolder extends RecyclerView.ViewHolder
    {

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
