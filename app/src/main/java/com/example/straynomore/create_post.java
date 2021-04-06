/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity is where user creates a post that will be displayed on map */
package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class create_post extends AppCompatActivity {

    private Button send;
    private EditText title, message, address;
    private FirebaseDatabase root;
    private DatabaseReference dbRef;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private ImageView postImg;
    private ForumHelper forumHelper;
    Uri imageUri;
    String imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        send = findViewById(R.id.btn_send);
        title = findViewById(R.id.txt_title);
        message = findViewById(R.id.txt_message);
        address = findViewById(R.id.txt_address);
        postImg = findViewById(R.id.img_post_img);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("messages");
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        postImg.setOnClickListener(v -> {
            setPostImg();
        });

        send.setOnClickListener(v -> {

            Toast.makeText(getApplicationContext(), "Saving Post...", Toast.LENGTH_SHORT).show();

            String forumTitle = title.getText().toString().trim();
            String forumMessage = message.getText().toString().trim();
            String forumAddress = address.getText().toString().trim();

            message.getText().clear();
            title.getText().clear();
            address.getText().clear();

            uploadImg();

            String user = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

            if(forumAddress.equals(""))
            {
               forumAddress = "null";
            }

            forumHelper = new ForumHelper(forumTitle, forumMessage, user, imageString, forumAddress);

            Date currentDate = Calendar.getInstance().getTime();

            dbRef.child(forumTitle).setValue(forumHelper);

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

    private String getExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImg()
    {
        StorageReference reference = storageReference.child(System.currentTimeMillis() + "."
                + getExtension(imageUri));

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Post saved!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), forum.class));
                finish();
            }
        });

        imageString = reference.getName();
    }

    private void setPostImg()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            postImg.setImageURI(imageUri);
        }
    }
}