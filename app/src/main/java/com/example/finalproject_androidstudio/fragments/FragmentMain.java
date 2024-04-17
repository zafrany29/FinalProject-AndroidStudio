package com.example.finalproject_androidstudio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.Toast;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.example.finalproject_androidstudio.activities.User;
import com.example.finalproject_androidstudio.recyclerview.BabysitterAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment {

    private FirebaseAuth mAuth;
    private BabysitterAdapter adapter;
    private RecyclerView recyclerView;
    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMain newInstance(String param1, String param2) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrieveDataFromFirebase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        // Find RecyclerView by its ID
        recyclerView = view.findViewById(R.id.recyclerView);

        // Create an instance of BabysitterAdapter and set it to the RecyclerView
        adapter = new BabysitterAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Assuming you're using a LinearLayoutManager, set it to the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Retrieve data from Firebase and populate RecyclerView
        retrieveDataFromFirebase();

        return view;
    }

    private void retrieveDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Babysitter> babysitters = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Babysitter babysitter = snapshot.getValue(Babysitter.class);
//                    if (babysitter != null && babysitter.getWhoAmI() == User.WhoAmI.BABYSITTER && babysitter.isVerified()) {
//                        babysitters.add(babysitter);
//                    }
//                }
//                adapter.setData(babysitters); // Set data to adapter
                Map<String, Babysitter> babysitterMap = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Babysitter babysitter = snapshot.getValue(Babysitter.class);
                    if (babysitter != null && babysitter.getWhoAmI() == User.WhoAmI.BABYSITTER && babysitter.isVerified()) {
                        babysitterMap.put(babysitter.getEmail(), babysitter); // Use ID as key
                    }
                }
                // Convert map values to list
                List<Babysitter> babysitters = new ArrayList<>(babysitterMap.values());
                adapter.setData(babysitters); // Set data to adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to read data from Firebase.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}