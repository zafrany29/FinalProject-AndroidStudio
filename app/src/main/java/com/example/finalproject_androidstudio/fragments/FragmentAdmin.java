package com.example.finalproject_androidstudio.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.example.finalproject_androidstudio.recyclerview.BabysitterAdminAdapter;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        adminRecycler = view.findViewById(R.id.admin_page_recyclerview);
        mAuth = FirebaseAuth.getInstance();

        // Initialize RecyclerView
        adminRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BabysitterAdminAdapter(new ArrayList<>());
        adminRecycler.setAdapter(adapter);

        // Retrieve and display babysitters
        retrieveBabysitters();

        return view;
    }

    private void retrieveBabysitters() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("whoAmI").equalTo("BABYSITTER");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Babysitter> babysitters = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Babysitter babysitter = snapshot.getValue(Babysitter.class);
                    if (babysitter != null && !babysitter.isVerified()) {
                        babysitters.add(babysitter);
                    }
                }
                adapter.setData(babysitters); // Set data to adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
