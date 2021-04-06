/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity is the home page where the user profile is shown. Navigation is on all pages as well */
package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Home extends AppCompatActivity {

    private FirebaseDatabase root;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private TextView username;
    String image;
    private static final String TAG = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new Handler().postDelayed(() -> findViewById(R.id.loadingPanel).setVisibility(View.GONE), 1000);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Button logout = findViewById(R.id.btn_logout);
        Button myPosts = findViewById(R.id.btn_myposts);
        ImageView profilePic = findViewById(R.id.img_profile_image);
        username = findViewById(R.id.txt_username);

        mAuth = FirebaseAuth.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Log.d(TAG, "UID >>>>> " + UID);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("users/" + UID);

        dbRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                UserHelper userHelper = dataSnapshot.getValue(UserHelper.class);
                assert userHelper != null;
                image = userHelper.getImg();
                username.setText("Welcome " + userHelper.getName() + "!");
                Log.d(TAG, "IMAGE >>>>> " + image);

                StorageReference pathReference = storageRef.child("Images/" + image);

                Glide.with(getApplicationContext()).load(pathReference).override(700,700).into(profilePic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        myPosts.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), forum.class);
            intent.putExtra("POST_TITLE", "null");
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.forum:
                        startActivity(new Intent(getApplicationContext(), forum.class));
                        finish();
                        return true;
                    case R.id.create_post:
                        startActivity(new Intent(getApplicationContext(), create_post.class));
                        finish();
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