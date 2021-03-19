package com.example.straynomore;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<InfoHelper> arrayList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView animalTxt, animalTitle;
    private Button close;
    private FirebaseDatabase root;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    public InfoAdapter(Context context, ArrayList<InfoHelper> arrayList)
    {
        this.arrayList = arrayList;
        this.context = context;
    }

    public InfoAdapter()
    {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_list, parent, false);
        return new InfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoHelper animal = arrayList.get(position);
        String animalName = animal.getTitle();
        holder.animal.setText(animalName);

        holder.parent.setOnClickListener(v -> {
            createNewDialog(animalName.toLowerCase());
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView animal;
        private final RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            animal = itemView.findViewById(R.id.animal);
            parent = itemView.findViewById(R.id.animal_layout);
        }
    }

    public void createNewDialog(String animal) {
        builder = new AlertDialog.Builder(context);
        View popupWindow = LayoutInflater.from(context).inflate(R.layout.popup, null);
        animalTitle = popupWindow.findViewById(R.id.txt_info_title);
        animalTxt = popupWindow.findViewById(R.id.txt_info);
        close = popupWindow.findViewById(R.id.btn_close);

        builder.setView(popupWindow);
        dialog = builder.create();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 200);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("animalinfo").child(animal);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                InfoHelper info = snapshot.getValue(InfoHelper.class);
                animalTxt.setText(info.getInfo());
                animalTitle.setText(info.getTitle());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        close.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }
}


