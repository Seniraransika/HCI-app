package com.example.cryptoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_FIRST_LAUNCH = "firstLaunch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean firstLaunch = preferences.getBoolean(KEY_FIRST_LAUNCH, true);

        if (!firstLaunch) {
            // Redirect to MainActivity if it's not the first launch
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_intro);

        initview();
    }

    private void initview() {
        ImageView goBtn = findViewById(R.id.imageView8);
        goBtn.setOnClickListener(v -> {
            // Update the preference to indicate that the splash screen has been shown
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(KEY_FIRST_LAUNCH, false);
            editor.apply();

            // Redirect to MainActivity
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        });
    }
}
