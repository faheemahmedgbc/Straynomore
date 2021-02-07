package com.example.straynomore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = findViewById(R.id.btn_login);

        TextView register = findViewById(R.id.txt_register);
        TextView forgot = findViewById(R.id.txt_forgot);

        forgot.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, forgot_password.class));
            finish();
        });

        register.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, register.class));
            finish();
        });

        login.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
        });
    }
}
