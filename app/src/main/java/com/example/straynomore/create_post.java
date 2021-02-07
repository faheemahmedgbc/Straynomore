package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class create_post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.create_post);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.home:
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                                return true;
                            case R.id.forum:
                                startActivity(new Intent(getApplicationContext(), forum.class));
                                finish();
                                return true;
                            case R.id.create_post:
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