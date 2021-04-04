/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This is the confirmation activity where user is sent email confirmation */
package com.example.straynomore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
//this class confirms user registrations
// It displays the users email address with a message telling the user to check their mail.
public class confirm_register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_register);

        TextView emailSent = findViewById(R.id.txt_email_sent);

        String email = "email@email.com";

        emailSent.setText("An email has been sent to " + email + " with account details");

        Button createProfile = findViewById(R.id.btn_save_profile);
        createProfile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), create_profile.class));
            finish();
        });
    }
}