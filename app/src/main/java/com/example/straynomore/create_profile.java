package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class create_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Button saveProfile = findViewById(R.id.btn_save_profile);
        ImageView profilePic= findViewById(R.id.img_profile_pic);

        profilePic.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 1);

        });

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