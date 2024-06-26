package com.example.cryptoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;


import com.example.cryptoapp.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initview();
        setVarible();
    }

    private void initview() {
        View goBtn = findViewById(R.id.main);
    }
    private void setVarible(){
        View goBtn = null;
        goBtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this,LoginActivity.class)));
    }
}