package com.example.finalproject_androidstudio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject_androidstudio.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        emailFocusListener();
        passwordFocusListener();
        phoneFocusListener();
    }

    private void submitForm() {
        // Validate form fields
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        // Validate email and password
        String emailError = validEmail();
        String passwordError = validPassword();

        if (emailError != null) {
            binding.emailContainer.setHelperText(emailError);
            return;
        }

        if (passwordError != null) {
            binding.passwordContainer.setHelperText(passwordError);
            return;
        }

        // Perform registration with Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                            // Move to the HomeActivity
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish(); // Optional: Finish the current activity to prevent the user from navigating back to it
                        } else {
                            // Registration failed
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void invalidForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid Form");
        StringBuilder message = new StringBuilder();
        if (binding.emailContainer.getHelperText() != null)
            message.append("\n\nEmail: ").append(binding.emailContainer.getHelperText());
        if (binding.passwordContainer.getHelperText() != null)
            message.append("\n\nPassword: ").append(binding.passwordContainer.getHelperText());
        if (binding.phoneContainer.getHelperText() != null)
            message.append("\n\nPhone: ").append(binding.phoneContainer.getHelperText());
        builder.setMessage(message.toString());
        builder.setPositiveButton("Okay", null);
        builder.show();
    }

    private void resetForm() {
        // Handle successful form submission
        Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show();
        // Clear form fields and reset helper text
        binding.emailEditText.setText("");
        binding.passwordEditText.setText("");
        binding.phoneEditText.setText("");

        binding.emailContainer.setHelperText(getString(R.string.required));
        binding.passwordContainer.setHelperText(getString(R.string.required));
        binding.phoneContainer.setHelperText(getString(R.string.required));
    }

    private void emailFocusListener() {
        binding.emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                binding.emailContainer.setHelperText(validEmail());
            }
        });
    }

    private String validEmail() {
        String emailText = binding.emailEditText.getText().toString().trim();
        if (emailText.isEmpty()) {
            return getString(R.string.required);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return getString(R.string.invalid_email);
        }
        return null;
    }

    private void passwordFocusListener() {
        binding.passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                binding.passwordContainer.setHelperText(validPassword());
            }
        });
    }

    private String validPassword() {
        String passwordText = binding.passwordEditText.getText().toString().trim();
        if (passwordText.isEmpty()) {
            return getString(R.string.required);
        }
        // Add additional password validation logic here (if needed)
        return null;
    }

    private void phoneFocusListener() {
        binding.phoneEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                binding.phoneContainer.setHelperText(validPhone());
            }
        });
    }

    private String validPhone() {
        String phoneText = binding.phoneEditText.getText().toString().trim();
        if (phoneText.isEmpty()) {
            return getString(R.string.required);
        }
        // Add additional phone number validation logic here (if needed)
        return null;
    }
}
