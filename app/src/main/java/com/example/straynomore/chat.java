package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class chat extends AppCompatActivity {

    private TextView chat, title, name;
    private ImageView imageUp;
    EditText commentInput;
    String nameComment, msgTitle;
    private Button sendComment, delete;
    private FirebaseDatabase root;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ArrayList<CommentHelper> commentHelpers;
    private static final String TAG = "chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        title = findViewById(R.id.txt_chat_title);
        chat = findViewById(R.id.txt_chat_message);
        name = findViewById(R.id.txt_user);
        sendComment = findViewById(R.id.btn_send_comment);
        delete = findViewById(R.id.btn_delete);
        commentInput = findViewById(R.id.comment);
        imageUp = findViewById(R.id.postedImg);

        recyclerView = findViewById(R.id.comment_section);

        commentHelpers = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        msgTitle = getIntent().getStringExtra("TITLE");
        String message = getIntent().getStringExtra("MESSAGE");
        String image = getIntent().getStringExtra("IMAGE");
        String UID = getIntent().getStringExtra("UID");

        String currentUID = mAuth.getUid();

        Log.d(TAG, "CURRENT USER >>>> " + currentUID + "MESSAGE USER >>>>> " + UID);

        if(!UID.equals(currentUID))
        {
            delete.setVisibility(View.GONE);
        }

        delete.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query query = ref.child("messages").orderByChild("title").equalTo(msgTitle);

            startActivity(new Intent(getApplicationContext(), forum.class));
            finish();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
        });

        StorageReference pathReference = storageRef.child("Images/" + image);

        Glide.with(this).load(pathReference).override(700,700).into(imageUp);

        title.setText(msgTitle);
        chat.setText(message);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("messages");

        DatabaseReference commentRef = root.getReference("messages").child(msgTitle).child("comments");
        String ID = commentRef.push().getKey();

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentHelpers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CommentHelper comment = dataSnapshot.getValue(CommentHelper.class);
                    Log.d(TAG, "Returned >>> " + comment);

                    if (comment != null) {
                        commentHelpers.add(comment);
                        ChatAdapter chatAdapter = new ChatAdapter(commentHelpers, getApplicationContext());
                        recyclerView.setAdapter(chatAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendComment.setOnClickListener(v -> {
            Log.d(TAG, "COMMENT SENT");
            String user = mAuth.getCurrentUser().getUid();

            Log.d(TAG, user);
            DatabaseReference reference = root.getReference("users").child(user);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserHelper user = snapshot.getValue(UserHelper.class);
                    nameComment = user.getName();

                    Log.d(TAG, nameComment);

                    String commentMessage = commentInput.getText().toString().trim();
                    Date currentDate = Calendar.getInstance().getTime();

                    CommentHelper commentHelper = new CommentHelper(nameComment, commentMessage);

                    dbRef.child(msgTitle).child("comments").push().setValue(commentHelper);
                    commentInput.getText().clear();
                    dismissKeyboard(com.example.straynomore.chat.this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "Failed to commit");
                }
            });
        });
    }
    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}