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
import android.widget.ProgressBar;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.example.finalproject_androidstudio.recyclerview.BabysitterAdminAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private List<Babysitter> babysittersList;
    private List<String> babysitterIds;
    private Handler handler;
    private static final long UPDATE_INTERVAL = 60000 * 10;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        adminRecycler = view.findViewById(R.id.admin_page_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adminRecycler.setLayoutManager(linearLayoutManager);
        progressBar = view.findViewById(R.id.admin_page_progressbar);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String adminId = currentUser != null ? currentUser.getUid() : "2FFOGYy2ce8aPmcltnSPAavVcFipq2";

        babysittersList = new ArrayList<>();
        babysitterIds = new ArrayList<>();
        adapter = new BabysitterAdminAdapter(getContext(), babysittersList, babysitterIds, adminId);
        adminRecycler.setAdapter(adapter);

        handler = new Handler(Looper.getMainLooper());
        setListeners();
        retrieveBabysitters();
        startPeriodicUpdates();

        return view;
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::retrieveBabysitters);
        adminRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                swipeRefreshLayout.setEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // Enable refresh only when the first item is completely visible (i.e., at the top)
            }
        });
    }

    private void startPeriodicUpdates() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                retrieveBabysitters();
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        }, UPDATE_INTERVAL);
    }

    private void retrieveBabysitters() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("whoAmI").equalTo("BABYSITTER");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                babysittersList.clear();
                babysitterIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Babysitter babysitter = snapshot.getValue(Babysitter.class);
                    if (babysitter != null && !babysitter.isVerified()) {
                        babysittersList.add(babysitter);
                        babysitterIds.add(snapshot.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
