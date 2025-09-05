package com.example.musicapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicapp.Fragments.SignInFragment;
import com.example.musicapp.R;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Load SignInFragment by default
        if (savedInstanceState == null) {
            setFragment(new SignInFragment(), false);
        }
    }

    // Method to switch fragments
    public void setFragment(Fragment fragment, boolean withAnimation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();



        transaction.replace(R.id.register_fram_layout, fragment);
        transaction.commit();
    }
}
