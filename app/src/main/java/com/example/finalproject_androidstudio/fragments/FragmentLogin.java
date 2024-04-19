package com.example.finalproject_androidstudio.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalproject_androidstudio.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FragmentLogin extends Fragment {

    private FirebaseAuth mAuth;

    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut(); // Ensure to sign out potentially cached users on app start

        if (mAuth.getCurrentUser() != null) {
            // User is signed in, check user type and navigate
            checkUserTypeAndNavigate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = view.findViewById(R.id.loginBbutton);
        Button regButton = view.findViewById(R.id.regButton);
        TextInputEditText emailEditText = view.findViewById(R.id.emailEditText);
        TextInputEditText passwordEditText = view.findViewById(R.id.passwordEditText);

        regButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_fragmentLogin_to_fragmentRegister);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Login failed. Please fill all fields", Toast.LENGTH_LONG).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                checkUserTypeAndNavigate();
                            } else {
                                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        return view;
    }

    private void checkUserTypeAndNavigate() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            DatabaseReference currentUserRef = usersRef.child(currentUser.getUid());

            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userType = dataSnapshot.child("whoAmI").getValue(String.class);
                        if ("ADMIN".equals(userType)) {
                            Navigation.findNavController(getView()).navigate(R.id.action_fragmentLogin_to_fragmentAdmin);
                        } else {
                            Navigation.findNavController(getView()).navigate(R.id.action_fragmentLogin_to_fragmentMain);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed to read user data. DataSnapshot does not exist.", Toast.LENGTH_LONG).show();
//                        Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot.getKey() + " does not exist.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No current user found.", Toast.LENGTH_LONG).show();
        }
    }


    private void queryUserByEmail(String email, DatabaseReference usersRef) {
        Query emailQuery = usersRef.orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userType = snapshot.child("whoAmI").getValue(String.class);
                        if ("ADMIN".equals(userType)) {
                            Navigation.findNavController(getView()).navigate(R.id.action_fragmentLogin_to_fragmentAdmin);
                            return;  // Found admin, navigate and exit
                        }
                    }
                    Navigation.findNavController(getView()).navigate(R.id.action_fragmentLogin_to_fragmentMain); // No admin found, navigate to main
                } else {
                    Toast.makeText(getActivity(), "No user found with this email.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database error on email query: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
