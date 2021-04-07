/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity is the recovery information that will allow user to recover info. */
package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class register extends AppCompatActivity {

    private FirebaseDatabase root;
    private DatabaseReference dbRef;
    private StorageReference storageReference;
    private Spinner userSpinner;
    private EditText username, email, password, conPassword;
    public ImageView profilePic;
    Uri imageUri;
    String imageString;
    private FirebaseAuth mAuth;
    private static final String TAG = "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        userSpinner = findViewById(R.id.user_spinner);
        username = findViewById(R.id.txt_reg_name);
        email = findViewById(R.id.txt_reg_email);
        password = findViewById(R.id.txt_reg_pass);
        conPassword = findViewById(R.id.txt_reg_pass_confirm);
        profilePic = findViewById(R.id.img_profile);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id != 0)
                {
                    profilePic.setVisibility(View.GONE);
                }
                else
                {
                    profilePic.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        profilePic.setOnClickListener(v -> {
            setPostImg();
        });

        register.setOnClickListener(v -> {

            root = FirebaseDatabase.getInstance();
            dbRef = root.getReference("users");

            String name = username.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String conPass = conPassword.getText().toString().trim();
            String userType = userSpinner.getSelectedItem().toString();

            if(userType.equals("Pet Owner"))
            {
                uploadImg();
            }

            if(name.isEmpty())
            {
                username.setError("Cannot be empty");
                username.requestFocus();
                return;
            }
            if(userEmail.isEmpty())
            {
                email.setError("Cannot be empty");
                email.requestFocus();
                return;
            }
            if(pass.isEmpty())
            {
                password.setError("Cannot be empty");
                password.requestFocus();
                return;
            }
            if(conPass.isEmpty())
            {
                conPassword.setError("Cannot be empty");
                conPassword.requestFocus();
                return;
            }

            if(!pass.equals(conPass))
            {
                conPassword.setError("Passwords must match");
                password.setError("Passwords must match");
                conPassword.requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(userEmail, conPass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                String user = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                                if(userType.equals("Animal Shelter"))
                                {
                                    imageString = "services/AnimalShelter.png";
                                }
                                else if(userType.equals("Animal Control"))
                                {
                                    imageString = "services/PetControl.png";
                                }

                                UserHelper userHelper = new UserHelper(name, userEmail, conPass, userType, imageString);

                                dbRef.child(user).setValue(userHelper);

                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(getApplicationContext(), confirm_register.class);
                                                intent.putExtra("EMAIL", userEmail);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
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
                Toast.makeText(getApplicationContext(), "Image saved!", Toast.LENGTH_SHORT).show();
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
            profilePic.setImageURI(imageUri);
        }
    }
}