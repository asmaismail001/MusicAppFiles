package com.example.musicapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvSignUp, tvReset;

    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.editTextTextEmailAddress);
        etPassword = view.findViewById(R.id.editTextTextPassword);
        btnSignIn = view.findViewById(R.id.button3);
        tvSignUp = view.findViewById(R.id.tvLogin);
        tvReset = view.findViewById(R.id.textview2);

        auth = FirebaseAuth.getInstance();

        // Sign Up link
        tvSignUp.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.register_fram_layout, new SignUpFragment())
                        .commit()
        );

        // Reset Password link
        tvReset.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.register_fram_layout, new ResetPasswordFragment())
                        .commit()
        );

        // Sign In button
        btnSignIn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid email");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        // Firebase Authentication sign in
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                        // ✅ Start MainActivity which contains HomeFragment with slider
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        requireActivity().finish(); // Close login/register activity
                    } else {
                        Toast.makeText(getContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
