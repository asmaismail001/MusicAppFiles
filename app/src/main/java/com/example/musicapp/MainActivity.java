package com.example.musicapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicapp.Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize FrameLayout
        frameLayout = findViewById(R.id.main_fram_layout);

        // Load HomeFragment initially
        setFragment(new HomeFragment());
    }

    // Method to set fragment
    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fram_layout, fragment);
        transaction.commit();
    }
}
