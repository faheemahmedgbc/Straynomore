/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This is the confirmation activity where user is sent email confirmation */
package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class confirm_register extends AppCompatActivity {
    private static final String TAG = "confirm_register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_register);

        TextView emailSent = findViewById(R.id.txt_email_sent);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = getIntent().getStringExtra("EMAIL");

        emailSent.setText("An email has been sent to " + email + " with account details");

        Button createProfile = findViewById(R.id.btn_save_profile);
        createProfile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }
}