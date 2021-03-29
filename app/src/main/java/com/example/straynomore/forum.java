/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity is where user will be able to chat with others regarding animals */
package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.security.cert.CertPathBuilder;
import java.util.ArrayList;

public class forum extends AppCompatActivity {

    private static final String CHANNEL_ID = "FORUM";
    private ArrayList<ForumHelper> names;
    ListAdapter listAdapter;
    RecyclerView recyclerView;
    FirebaseDatabase root;
    DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private static final String TAG = "forum";
    Boolean showNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        TextView test = findViewById(R.id.txt_test);

        names = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("messages");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                names.clear();
                if(showNotification) {
                    notification();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ForumHelper forum = ds.getValue(ForumHelper.class);
                    names.add(forum);
                }
                listAdapter = new ListAdapter(getApplicationContext(), names);
                recyclerView.setAdapter(listAdapter);
                //use your Data here
                showNotification = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d(TAG, "Failed to connect to database");
            }
        });

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.forum);
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

    private void notification(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setContentText("A new post!")
                .setSmallIcon(R.drawable.logo_animal)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());
    }
}