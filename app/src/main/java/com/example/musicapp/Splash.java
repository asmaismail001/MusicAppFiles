package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView logo = findViewById(R.id.logo);
        TextView logoMt = findViewById(R.id.logoMt);

        // Load animation
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

        // Apply animation to both TextViews
        logo.startAnimation(anim);
        logoMt.startAnimation(anim);

        // Move to MainActivity after animation
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash.this, Registration.class));
            finish();
        }, SPLASH_DURATION);
    }
}
