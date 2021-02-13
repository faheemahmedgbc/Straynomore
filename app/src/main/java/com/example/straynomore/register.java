package com.example.straynomore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = findViewById(R.id.btn_register);

        register.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), confirm_register.class));
            finish();
        });
    }
}