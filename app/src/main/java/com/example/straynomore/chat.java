package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class chat extends AppCompatActivity {

    private TextView chat, title, name;
    EditText commentInput;
    String nameComment, msgTitle;
    private Button sendComment;
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
        commentInput = findViewById(R.id.comment);

        recyclerView = findViewById(R.id.comment_section);

        commentHelpers = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        msgTitle = getIntent().getStringExtra("TITLE");
        String message = getIntent().getStringExtra("MESSAGE");

        title.setText(msgTitle);
        chat.setText(message);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "Failed to commit");
                }
            });
        });
    }
}