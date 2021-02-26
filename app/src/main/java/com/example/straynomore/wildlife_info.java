package com.example.straynomore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class wildlife_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wildlife_info);

        ImageView backToMap = findViewById(R.id.btn_back_to_map);

        backToMap.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), map.class));
            finish();
        });
    }
}