package com.example.finalproject_androidstudio.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalproject_androidstudio.R;
import com.example.finalproject_androidstudio.activities.MainActivity;
import com.example.finalproject_androidstudio.activities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {

    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Define all fields
        MainActivity mainActivity = (MainActivity) getActivity();
        User user = mainActivity.getUser();

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

        // Set fields
        regProfession.setVisibility(View.GONE);
        registration_years_experience.setVisibility(View.GONE);

        firstPersonName.setText(user.getFname());
        lastPersonName.setText(user.getLname());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regGender.setAdapter(adapter);
        String valueToSet = user.getGender();
        int position = adapter.getPosition(valueToSet); // Find the position of the value
        regGender.setSelection(position);

        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regLocation.setAdapter(adapter);
        valueToSet = user.getLocation();
        position = adapter.getPosition(valueToSet);
        regLocation.setSelection(position);

        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.user_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regUserType.setAdapter(adapter);
        valueToSet = user.getUserType();
        position = adapter.getPosition(valueToSet);
        regUserType.setSelection(position);

        if(valueToSet.equals("נותן שירות")){
            regProfession.setVisibility(View.VISIBLE);
            registration_years_experience.setVisibility(View.VISIBLE);

            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.profession_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regProfession.setAdapter(adapter);
            valueToSet = user.getProfession();
            position = adapter.getPosition(valueToSet);
            regProfession.setSelection(position);

            registration_years_experience.setText(user.getYears());
        }

        emailRegText.setText(user.getEmail());
        passRegText.setText(user.getPassword());
        regTextPhone.setText(user.getPhone());
        regExtraText.setText(user.getText());


        // Buttons
        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regProfession.setVisibility(View.GONE);
                registration_years_experience.setVisibility(View.GONE);


                firstPersonName.setText(user.getFname());
                lastPersonName.setText(user.getLname());

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.gender_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                regGender.setAdapter(adapter);
                String valueToSet = user.getGender();
                int position = adapter.getPosition(valueToSet); // Find the position of the value
                regGender.setSelection(position);

                adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.location_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                regLocation.setAdapter(adapter);
                valueToSet = user.getLocation();
                position = adapter.getPosition(valueToSet);
                regLocation.setSelection(position);

                adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.user_type_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                regUserType.setAdapter(adapter);
                valueToSet = user.getUserType();
                position = adapter.getPosition(valueToSet);
                regUserType.setSelection(position);

                if(valueToSet.equals("נותן שירות")){
                    regProfession.setVisibility(View.VISIBLE);
                    registration_years_experience.setVisibility(View.VISIBLE);

                    adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.profession_array, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    regProfession.setAdapter(adapter);
                    valueToSet = user.getProfession();
                    position = adapter.getPosition(valueToSet);
                    regProfession.setSelection(position);

                    registration_years_experience.setText(user.getYears());
                }

                emailRegText.setText(user.getEmail());
                passRegText.setText(user.getPassword());
                regTextPhone.setText(user.getPhone());
                regExtraText.setText(user.getText());
            }
        });


        Button updateButton = (Button) view.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming the user is already authenticated and you're just updating their info
                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                if (firebaseUser != null) {
                    // Get user inputs from EditText fields
                    String fname = firstPersonName.getText().toString().trim();
                    String lname = lastPersonName.getText().toString().trim();
                    String gender = regGender.getSelectedItem().toString();
                    String location = regLocation.getSelectedItem().toString();
                    String userType = regUserType.getSelectedItem().toString();
                    String profession = regProfession.getVisibility() != View.GONE ? regProfession.getSelectedItem().toString() : null;
                    String years = registration_years_experience.getVisibility() != View.GONE ? registration_years_experience.getText().toString().trim() : null;
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
                        Toast.makeText(getActivity(), "Updating failed. Fill all fields", Toast.LENGTH_LONG).show();
                    } else {
                        User user;
                        if (userType.equals("נותן שירות")) { // Use .equals for string comparison
                            if (years.isEmpty() || regProfession.getSelectedItemPosition() == 0) {
                                Toast.makeText(getActivity(), "Updating failed. Fill all fields", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                user = new User(fname, lname, profession, gender, location, userType, email, password, phone, extraText, years);
                            }
                        } else {
                            user = new User(fname, lname, gender, location, userType, email, password, phone, extraText);
                        }

                        // Define a HashMap or another data structure for the fields you want to update
                        Map<String, Object> userUpdates = new HashMap<>();
                        userUpdates.put("fname", fname);
                        userUpdates.put("lname", lname);
                        userUpdates.put("gender", gender);
                        userUpdates.put("location", location);
                        userUpdates.put("userType", userType);
                        userUpdates.put("profession", profession); // Consider checking if this should be updated only for certain user types
                        userUpdates.put("years", years); // Same consideration as above
                        userUpdates.put("phone", phone);
                        userUpdates.put("extraText", extraText);

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

                        // Update the user data
                        userRef.updateChildren(userUpdates).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "User data updated", Toast.LENGTH_SHORT).show();
                                // Optionally, navigate or update UI as needed
                            } else {
                                Toast.makeText(getActivity(), "Failed to update user data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}