package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Button logout = findViewById(R.id.btn_logout);
        ImageView profilePic = findViewById(R.id.img_profile_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        Uri image = user.getPhotoUrl();

        profilePic.setImageURI(image);

        logout.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.forum:
                        startActivity(new Intent(getApplicationContext(), forum.class));
                        finish();
                        return true;
                    case R.id.create_post:
                        startActivity(new Intent(getApplicationContext(), create_post.class));
                        finish();
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), map.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}