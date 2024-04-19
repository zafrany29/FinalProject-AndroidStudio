package com.example.finalproject_androidstudio.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.Babysitter;
import com.example.finalproject_androidstudio.activities.MyUser;
import com.example.finalproject_androidstudio.databinding.FragmentMainBinding;
import com.example.finalproject_androidstudio.recyclerview.BabysitterAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Spinner locationSpinner;
    private List<String> locationList;
    private ArrayAdapter<String> spinnerAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView greetingText;

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

        // Retrieve data from Firebase and populate RecyclerView
        retrieveDataFromFirebase("");
        // Initialize greeting text view
        greetingText = view.findViewById(R.id.greeting_text);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve full name
                    String fullName = dataSnapshot.child("fullName").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    retrieveDataFromFirebase(location);


                    // Set greeting text
                    greetingText.setText("שלום " + fullName);
                } else {
                    // User data does not exist
                    greetingText.setText("תקלה");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // Setup logout button
        Button logoutButton = view.findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the current user
                mAuth.signOut();
                // Navigate to the login screen
                Navigation.findNavController(v).navigate(R.id.action_fragmentMain_to_fragmentLogin);
            }
        });

        // Setup location spinner
        locationSpinner = view.findViewById(R.id.location_spinner);
        setupSpinner();
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

        ImageButton profileButton = view.findViewById(R.id.profile_btn);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragmentMain_to_fragmentProfile);
            }
        });

        FragmentMainBinding binding = FragmentMainBinding.inflate(inflater, container, false);

        binding.profileBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_fragmentMain_to_fragmentProfile);
        });

        return view;
    }

    private void retrieveDataFromFirebase(String locationFilter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Babysitter> babysitterMap = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Babysitter babysitter = snapshot.getValue(Babysitter.class);
                    if (babysitter != null && babysitter.getWhoAmI() == MyUser.WhoAmI.BABYSITTER && babysitter.isVerified()) {
                        if (locationFilter.isEmpty() || locationFilter.equals("כל המיקומים") || babysitter.getLocation().equalsIgnoreCase(locationFilter)) {
                            babysitterMap.put(babysitter.getEmail(), babysitter); // Use ID as key
                        }
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
    private void setupSpinner() {
        locationList = new ArrayList<>();
        locationList.add("כל המיקומים"); // Default option
        locationList.add("מחוז צפון");
        locationList.add("מחוז חיפה");
        locationList.add("מחוז תל אביב");
        locationList.add("מחוז המרכז");
        locationList.add("מחוז ירושלים");
        locationList.add("מחוז הדרום");

        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, locationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(spinnerAdapter);

        // Retrieve current user's location from the database
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String currentUserLocation = dataSnapshot.child("location").getValue(String.class);
                    if (currentUserLocation != null && !currentUserLocation.isEmpty()) {
                        // Set the selected item in the spinner to the current user's location
                        int index = locationList.indexOf(currentUserLocation);
                        if (index != -1) {
                            locationSpinner.setSelection(index);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        // Set spinner item selection listener
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLocation = locationList.get(position);
                retrieveDataFromFirebase(selectedLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private String sanitizeEmail(String email) {
        if (email != null) {
            return email.replace(".", ",");
        }
        return null;
    }
}