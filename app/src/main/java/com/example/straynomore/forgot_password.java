/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity contains the forgot password. It will allow user to send an email of forgotten password */
package com.example.straynomore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

public class forgot_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Button resetPassword = findViewById(R.id.btn_email_password);

        resetPassword.setOnClickListener(v -> {
            Snackbar.make(findViewById(R.id.forgot_password), "Password reset email sent! " +
                            "\nRedirecting to log in page",
                    Snackbar.LENGTH_LONG)
                    .show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }, 3000);
        });

        ImageView back = findViewById(R.id.img_back);

        back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }
}