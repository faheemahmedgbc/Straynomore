package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class create_profile extends AppCompatActivity {

    private ImageView profilePic;
    private Uri profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Button saveProfile = findViewById(R.id.btn_save_profile);
        profilePic = findViewById(R.id.img_profile_pic);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        profilePic.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 1000);

            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/straynomore-c0efc.appspot.com/o/535de1c74790e1a.jpg?alt=media&token=a3932f58-344b-4e0a-92ae-af1505388b6c")).build();

            FirebaseUser user = mAuth.getCurrentUser();
            assert user != null;
            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Picture updated!", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        });

        saveProfile.setOnClickListener(v -> {
            Snackbar.make(findViewById(R.id.save_profile), "Profile saved! " +
                            "\nRedirecting to home",
                    Snackbar.LENGTH_LONG)
                    .show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }
            }, 3000);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                assert data != null;
                Uri profileImg = data.getData();
                profilePic.setImageURI(profileImg);
                profileUri = profileImg;
            }
        }
    }
}