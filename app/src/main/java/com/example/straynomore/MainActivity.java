/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity is the initial login page. The login is authenticated through firebase to verify account information.
    From this page, user can register or login.*/
package com.example.straynomore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = findViewById(R.id.btn_login);

        TextView register = findViewById(R.id.txt_register);
        TextView forgot = findViewById(R.id.txt_forgot);

        mAuth = FirebaseAuth.getInstance();

        forgot.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, forgot_password.class));
            finish();
        });

        register.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, register.class));
            finish();
        });

        login.setOnClickListener(v -> {

            username = findViewById(R.id.txt_username_login);
            password = findViewById(R.id.txt_password_login);

            String userIn = username.getText().toString();
            String passIn = password.getText().toString();

            if(userIn.isEmpty())
            {
                username.setError("Field cannot be empty");
                username.requestFocus();
                return;
            }
            if(passIn.isEmpty())
            {
                password.setError("Field cannot be empty");
                password.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(userIn, passIn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified())
                        {
                            startActivity(new Intent(MainActivity.this, Home.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Email has not been verified.", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "User", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        });
    }

}
