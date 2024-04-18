package com.example.finalproject_androidstudio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.example.finalproject_androidstudio.recyclerview.BabysitterAdminAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FragmentAdmin extends Fragment {

    RecyclerView adminRecycler;
    private BabysitterAdminAdapter adapter;
    private List<Babysitter> babysitters;
    private List<String> babysitterIds;  // To store the Firebase UIDs

    private Handler handler;
    private static final long UPDATE_INTERVAL = 60000 * 10; // 10 minute in milliseconds

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        adminRecycler = view.findViewById(R.id.admin_page_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        adminRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the lists
        babysitters = new ArrayList<>();
        babysitterIds = new ArrayList<>();

        adapter = new BabysitterAdminAdapter(getContext(), babysitters, babysitterIds);
        adminRecycler.setAdapter(adapter);

        // Initialize the handler
        handler = new Handler(Looper.getMainLooper());

        // Setup the SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveBabysitters(); // Refresh the list when swiped
            }
        });

        // Retrieve and display babysitters initially
        retrieveBabysitters();

        // Start periodic updates
        startPeriodicUpdates();

        return view;
    }

    private void startPeriodicUpdates() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                retrieveBabysitters(); // Retrieve and display babysitters periodically
                handler.postDelayed(this, UPDATE_INTERVAL); // Schedule the next update
            }
        }, UPDATE_INTERVAL);
    }

    private void retrieveBabysitters() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("whoAmI").equalTo("BABYSITTER");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                babysitters.clear();  // Clear the current list
                babysitterIds.clear();  // Clear the UID list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Babysitter babysitter = snapshot.getValue(Babysitter.class);
                    if (babysitter != null && !babysitter.isVerified()) {
                        babysitters.add(babysitter);
                        babysitterIds.add(snapshot.getKey());  // Store the Firebase UID
                    }
                }
                adapter.notifyDataSetChanged();  // Notify the adapter to refresh the data
                swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
    }
}
