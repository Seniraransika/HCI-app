package com.example.cryptoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    ProgressBar progressBar;
    ImageView profileImage;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri imageUri;
    String imageUrl;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        progressBar = findViewById(R.id.progress_bar);
        profileImage = findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }

    // icon to open images to user propfile
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override

    // upload image to firebase
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }
// firebase data danava
    private void registerUser() {
        final String name = signupName.getText().toString();
        final String email = signupEmail.getText().toString();
        final String username = signupUsername.getText().toString();
        String password = signupPassword.getText().toString();

        if (validateInput(name, email, username, password)) {
            progressBar.setVisibility(View.VISIBLE); // Show the ProgressBar

            // create new user in firebase auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE); // Hide the ProgressBar
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    String uid = user.getUid();
                                    uploadImageAndSaveData(uid, name, username, email);
                                }
                            } else {
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
// upload image to firestore database and get access url from friebase storage bucket
    private void uploadImageAndSaveData(final String uid, final String name, final String username, final String email) {
        if (imageUri != null) {
            StorageReference fileRef = storageRef.child("profile_images/" + uid);
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> downloadUriTask = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUriTask.addOnSuccessListener(uri -> {
                            imageUrl = uri.toString();
                            saveUserToFirestore(uid, name, username, email, imageUrl);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(SignupActivity.this, "Failed to upload image.",
                                    Toast.LENGTH_SHORT).show();
                            saveUserToFirestore(uid, name, username, email, null);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SignupActivity.this, "Failed to upload image.",
                                Toast.LENGTH_SHORT).show();
                        saveUserToFirestore(uid, name, username, email, null);
                    });
        } else {
            saveUserToFirestore(uid, name, username, email, null);
        }
    }

// save data to firestore database
    private void saveUserToFirestore(String uid, String name, String username, String email, String imageUrl) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("username", username);
        user.put("email", email);
        user.put("userId", uid);

        if (imageUrl != null) {
            user.put("profileImageUrl", imageUrl);
        }

        progressBar.setVisibility(View.VISIBLE); // Show the ProgressBar
        db.collection("users").document(uid).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE); // Hide the ProgressBar
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "You have signed up successfully!",
                                    Toast.LENGTH_SHORT).show();
                            navigateToLogin();
                        } else {
                            Toast.makeText(SignupActivity.this, "Error saving user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean validateInput(String name, String email, String username, String password) {
        if (name.isEmpty()) {
            signupName.setError("Name cannot be empty");
            return false;
        } else if (email.isEmpty()) {
            signupEmail.setError("Email cannot be empty");
            return false;
        } else if (username.isEmpty()) {
            signupUsername.setError("Username cannot be empty");
            return false;
        } else if (password.isEmpty()) {
            signupPassword.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
