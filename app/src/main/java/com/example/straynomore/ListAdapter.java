package com.example.straynomore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private static final String CHANNEL_ID = "FORUM";
    private Context context;
    private ArrayList<ForumHelper> arrayList;
    private static final String TAG = "ListAdapter";

    public ListAdapter(Context context, ArrayList<ForumHelper> arrayList)
    {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForumHelper forumHelper = arrayList.get(position);
        holder.name.setText(forumHelper.getTitle());

        holder.parentLayout.setOnClickListener(v -> {

            Intent intent = new Intent(context, chat.class);
            intent.putExtra("TITLE", forumHelper.getTitle());
            intent.putExtra("MESSAGE", forumHelper.getMessage());
            intent.putExtra("IMAGE", forumHelper.getImage());
            intent.putExtra("UID", forumHelper.getUid());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_list_name);
            parentLayout = itemView.findViewById(R.id.layout);
        }
    }
}


