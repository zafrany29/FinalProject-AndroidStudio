package com.example.finalproject_androidstudio.fragments;

import com.example.finalproject_androidstudio.activities.MainActivity;
import com.example.finalproject_androidstudio.activities.User;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalproject_androidstudio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTwo extends Fragment {
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTwo newInstance(String param1, String param2) {
        FragmentTwo fragment = new FragmentTwo();
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
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        EditText firstPersonName = view.findViewById(R.id.regTextFirstName);
        EditText lastPersonName = view.findViewById(R.id.regTextLastName);
        Spinner regGender = view.findViewById(R.id.regGender);
        Spinner regLocation = view.findViewById(R.id.regLocation);

        Spinner regUserType = view.findViewById(R.id.regUserType);
        ArrayAdapter<CharSequence> adapterUserType = ArrayAdapter.createFromResource(getContext(),
                R.array.user_type_array, android.R.layout.simple_spinner_item);
        adapterUserType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regUserType.setAdapter(adapterUserType);

        Spinner regProfession = view.findViewById(R.id.regProfession);
        EditText registration_years_experience = view.findViewById(R.id.registration_years_experience);
        EditText emailRegText = view.findViewById(R.id.regTextEmailAddress);
        EditText passRegText = view.findViewById(R.id.regTextPassword);
        EditText regTextPhone = view.findViewById(R.id.regTextPhone);
        EditText regExtraText = view.findViewById(R.id.regExtraText);

        regProfession.setVisibility(View.GONE);
        registration_years_experience.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        String email = bundle.getString("emailText");
        String password = bundle.getString("passText");

        emailRegText.setText(email);
        passRegText.setText(password);

        // Set listener on the Spinner
        regUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // If the position matches the condition to show the email field
                if (position == 2) {
                    regProfession.setVisibility(View.VISIBLE);
                    registration_years_experience.setVisibility(View.VISIBLE);
                } else {
                    regProfession.setVisibility(View.GONE); // Or View.INVISIBLE
                    registration_years_experience.setVisibility(View.GONE); // Or View.INVISIBLE
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optionally handle the case where nothing is selected
            }
        });

        // Register
        Button buttonOne = (Button) view.findViewById(R.id.buttonLogout);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs from EditText fields
                String fname = firstPersonName.getText().toString().trim();
                String lname = lastPersonName.getText().toString().trim();
                String gender = regGender.getSelectedItem().toString();
                String location = regLocation.getSelectedItem().toString();
                String userType = regUserType.getSelectedItem().toString();
                String profession = regProfession.getSelectedItem().toString();
                String years = registration_years_experience.getText().toString().trim();
                String phone = regTextPhone.getText().toString().trim();
                String extraText = regExtraText.getText().toString().trim();
                String email = emailRegText.getText().toString().trim();
                String password = passRegText.getText().toString().trim();

                if ((fname == null || fname.isEmpty()) ||
                        (lname == null || lname.isEmpty()) ||
                        (extraText == null || extraText.isEmpty()) ||
                        (password == null || password.isEmpty()) ||
                        (email == null || email.isEmpty()) ||
                        (phone == null || phone.isEmpty()) ||
                        regGender.getSelectedItemPosition() == 0 ||
                        regLocation.getSelectedItemPosition() == 0 ||
                        regUserType.getSelectedItemPosition() == 0
                ) {
                    Toast.makeText(getActivity(), "Registration failed. Fill all fields", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success
                                        Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_LONG).show();
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                        if (firebaseUser != null) {
                                            // Create a new User object
                                            User user = null;
                                            Log.d("error", userType);
                                            if (userType == "נותן שירות") {
                                                if((years == null || years.isEmpty()) ||
                                                        regProfession.getSelectedItemPosition() == 0){
                                                    Toast.makeText(getActivity(), "Registration failed. Fill all fields", Toast.LENGTH_LONG).show();
                                                }
                                                else {
                                                    user = new User(fname, lname, profession, gender, location, userType, email, password, phone, extraText, years);
                                                }
                                            } else {
                                                user = new User(fname, lname, gender, location, userType, email, password, phone, extraText);
                                            }

                                            MainActivity mainActivity = (MainActivity) getActivity();
                                            mainActivity.setUser(user);

                                            // Get a reference to the database
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference usersRef = database.getReference("users");

                                            // Save the User object under the UID
                                            usersRef.child(firebaseUser.getUid()).setValue(user)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "User data saved", Toast.LENGTH_SHORT).show();
                                                                // If you want to pass the email to another fragment, you can do so
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("name", fname + " " + lname);
                                                                Navigation.findNavController(view).navigate(R.id.action_fragmentTwo_to_fragmentThree, bundle);
                                                            } else {
                                                                Toast.makeText(getActivity(), "Failed to save user data", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // If registration fails, display a message to the user
                                        Toast.makeText(getActivity(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });


        // Logout button
        Button buttonLogout = (Button) view.findViewById(R.id.returnButton);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentTwo_to_fragmentOne);
            }
        });

        return view;
    }

}