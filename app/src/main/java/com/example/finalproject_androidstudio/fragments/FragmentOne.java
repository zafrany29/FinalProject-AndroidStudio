package com.example.finalproject_androidstudio.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.MainActivity;
import com.example.finalproject_androidstudio.activities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentOne extends Fragment {
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOne.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOne newInstance(String param1, String param2) {
        FragmentOne fragment = new FragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        EditText emailTextView = view.findViewById(R.id.TextEmailAddress);
        EditText passTextView = view.findViewById(R.id.TextPassword);

        Button loginButton = (Button) view.findViewById(R.id.loginBbutton);
        Button regButton = (Button) view.findViewById(R.id.regButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = emailTextView.getText().toString();
                String passStr = passTextView.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("emailText", emailStr);
                bundle.putString("passText", passStr);

                Navigation.findNavController(view).navigate(R.id.action_fragmentOne_to_fragmentTwo, bundle);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                String password = passTextView.getText().toString();

                if((email == null || email.isEmpty()) || (password == null || password.isEmpty())){
                    Toast.makeText(getActivity(), "login failed. fill all fields", Toast.LENGTH_LONG).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser currentUser = mAuth.getCurrentUser();

                                    if (currentUser != null) {
                                        // Get UID of the logged-in user
                                        String uid = currentUser.getUid();

                                        // Fetch user details from the database
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference usersRef = database.getReference("users");
                                        DatabaseReference userRef = usersRef.child(uid);

                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                // Assuming you have a User class that matches your database structure
                                                User user = dataSnapshot.getValue(User.class);
                                                String name = "";

                                                MainActivity mainActivity = (MainActivity) getActivity();
                                                mainActivity.setUser(user);

                                                if (user != null) {
                                                    name = user.fname + " " + user.lname;
                                                    Log.d("LoginSuccess", "User's name: " + name);
                                                    Toast.makeText(getActivity(), "login ok", Toast.LENGTH_LONG).show();
                                                }

                                                Bundle bundle = new Bundle();
                                                bundle.putString("name", name);
                                                Navigation.findNavController(view).navigate(R.id.action_fragmentOne_to_fragmentThree, bundle);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Toast.makeText(getActivity(), "login failed1", Toast.LENGTH_LONG).show();                                                // Handle errors
                                            }
                                        });
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getActivity(), "login failed2", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }
            }
        });

        return view;
    }
}