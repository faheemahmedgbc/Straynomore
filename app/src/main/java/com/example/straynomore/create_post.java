/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity is where user creates a post that will be displayed on map */
package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class create_post extends AppCompatActivity {

    private Button send;
    private EditText title, message;
    private FirebaseDatabase root;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        send = findViewById(R.id.btn_send);
        title = findViewById(R.id.txt_title);
        message = findViewById(R.id.txt_message);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("messages");

        send.setOnClickListener(v -> {
            String forumTitle = title.getText().toString().trim();
            String forumMessage = message.getText().toString().trim();

            String user = mAuth.getCurrentUser().getUid();

            ForumHelper forumHelper = new ForumHelper(forumTitle, forumMessage, user);

            Date currentDate = Calendar.getInstance().getTime();

            dbRef.child(String.valueOf(currentDate)).setValue(forumHelper);

        });

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