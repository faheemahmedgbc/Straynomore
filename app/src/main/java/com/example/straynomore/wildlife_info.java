package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class wildlife_info extends AppCompatActivity {

    private FirebaseDatabase root;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private ArrayList<InfoHelper> animals;
    InfoAdapter infoAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wildlife_info);

        recyclerView = findViewById(R.id.animal_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        animals = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("animalinfo");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                animals.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    InfoHelper info = ds.getValue(InfoHelper.class);
                    animals.add(info);
                }
                infoAdapter = new InfoAdapter(wildlife_info.this, animals);
                recyclerView.setAdapter(infoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });

        ImageView backToMap = findViewById(R.id.btn_back_to_map);

        backToMap.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), map.class));
            finish();
        });
    }
}