package com.example.finalproject_androidstudio.fragments;

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

public class FragmentLogin extends Fragment {

    private FirebaseAuth mAuth;

    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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
                else
                {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        // Navigate to the desired fragment here
                                        Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLogin_to_fragmentMain);
                                        // Finish the current activity to prevent going back to the login screen on back press
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
