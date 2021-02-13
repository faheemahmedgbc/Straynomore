package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class create_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Button saveProfile = findViewById(R.id.btn_save_profile);

        saveProfile.setOnClickListener(v -> {
            Snackbar.make(findViewById(R.id.save_profile), "Profile saved! " +
                            "\nRedirecting to home",
                    Snackbar.LENGTH_LONG)
                    .show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }
            }, 3000);
        });
    }
}