package com.example.finalproject_androidstudio.fragments;

import android.os.Bundle;
import android.util.Log;
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
        mAuth.signOut();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Navigation.findNavController(view).navigate(R.id.action_fragmentLogin_to_fragmentMain);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = view.findViewById(R.id.loginBbutton);
        Button regButton = view.findViewById(R.id.regButton);
        TextInputEditText emailEditText = view.findViewById(R.id.emailEditText);
        TextInputEditText passwordEditText = view.findViewById(R.id.passwordEditText);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = emailEditText.getText().toString();
                String passStr = passwordEditText.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("emailText", emailStr);
                bundle.putString("passText", passStr);

                Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLogin_to_fragmentRegister, bundle);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty())
                    Toast.makeText(getActivity(), "Login failed. Please fill all fields", Toast.LENGTH_LONG).show();
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

                                        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    // Data exists at the specified location
                                                    String userType = dataSnapshot.child("whoAmI").getValue(String.class);
                                                    if (userType != null && userType.equals("ADMIN")) {
                                                        // Navigate to the desired fragment for admin
                                                        Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLogin_to_fragmentAdmin);
                                                    } else {
                                                        // Navigate to the desired fragment for regular user
                                                        Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLogin_to_fragmentMain);
                                                    }
                                                } else {
                                                    // No data found at the specified location
                                                    // Search for a user with the same email
                                                    String userEmail = mAuth.getCurrentUser().getEmail();
                                                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                                                    Query query = usersRef.orderByChild("email").equalTo(userEmail);
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                                    // Proceed with linking user authentication for each child node
                                                                    String userKey = childSnapshot.getKey();
                                                                    String userUid = mAuth.getCurrentUser().getUid();
                                                                    usersRef.child(userKey).child("uid").setValue(userUid);

                                                                    // Retrieve the value of "whoAmI" from the current child node
                                                                    String userType = childSnapshot.child("whoAmI").getValue(String.class);
                                                                    if (userType != null && userType.equals("ADMIN")) {
                                                                        // Navigate to the desired fragment for admin
                                                                        Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLogin_to_fragmentAdmin);
                                                                        return; // Exit the loop if admin is found
                                                                    }
                                                                }
                                                                // If no child with "whoAmI" is found, navigate to regular user fragment
                                                                Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLogin_to_fragmentMain);

                                                                // Show success toast
                                                                Toast.makeText(getActivity(), "User authentication linked successfully!", Toast.LENGTH_LONG).show();

                                                                // Proceed with the appropriate action (e.g., navigate to fragment)
                                                                // You can retrieve user data here if needed
                                                            } else {
                                                                // No data found at the specified location
                                                                // Show toast indicating user not found
                                                                Toast.makeText(getActivity(), "User not found in the database", Toast.LENGTH_LONG).show();

                                                                // Handle the case as per your application logic
                                                            }
                                                        }


                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            // Show error toast
                                                            Toast.makeText(getActivity(), "Error searching for user: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                        }

                                                    });
                                                }
                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Handle errors here
//                                                Log.e("Firebase", "Error fetching user data", databaseError.toException());
                                            }
                                        });
                                    }
                                } else {
                                    // Clear the password field and display error message
                                    passwordEditText.setText("");
                                    Toast.makeText(getActivity(), "Wrong email or password. Please try again", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        return view;
    }
}
