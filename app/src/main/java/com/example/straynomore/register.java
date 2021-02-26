package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    FirebaseDatabase root;
    DatabaseReference dbRef;
    Spinner userSpinner;
    EditText username, email, password, conPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();

        userSpinner = findViewById(R.id.user_spinner);
        username = findViewById(R.id.txt_reg_name);
        email = findViewById(R.id.txt_reg_email);
        password = findViewById(R.id.txt_reg_pass);
        conPassword = findViewById(R.id.txt_reg_pass_confirm);

        register.setOnClickListener(v -> {

            root = FirebaseDatabase.getInstance();
            dbRef = root.getReference("users");

            String name = username.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String conPass = conPassword.getText().toString().trim();

            String userType = userSpinner.getSelectedItem().toString();

            if(name.isEmpty())
            {
                username.setError("Cannot be empty");
                username.requestFocus();
                return;
            }
            if(userEmail.isEmpty())
            {
                email.setError("Cannot be empty");
                email.requestFocus();
                return;
            }
            if(pass.isEmpty())
            {
                password.setError("Cannot be empty");
                password.requestFocus();
                return;
            }
            if(conPass.isEmpty())
            {
                conPassword.setError("Cannot be empty");
                conPassword.requestFocus();
                return;
            }

            if(!pass.equals(conPass))
            {
                conPassword.setError("Passwords must match");
                password.setError("Passwords must match");
                conPassword.requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(userEmail, conPass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                String user = mAuth.getCurrentUser().getUid();

                                UserHelper userHelper = new UserHelper(name, userEmail, conPass, userType);

                                dbRef.child(user).setValue(userHelper);

                                startActivity(new Intent(getApplicationContext(), confirm_register.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        });
    }
}