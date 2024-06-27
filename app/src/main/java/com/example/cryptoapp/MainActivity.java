package com.example.cryptoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private TextView userNameTextView, userEmailTextView;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private ListenerRegistration userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize Views
        userNameTextView = findViewById(R.id.userNameview);
        userEmailTextView = findViewById(R.id.userEmailview);

        recyclerViewInit();
        setupProfileIconClick();
        fetchUserData();
    }

    private void recyclerViewInit() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.view);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Uncomment and complete the following lines if you have the necessary classes
        // ArrayList<CryptoWallet> cryptoWalletArrayList = new ArrayList<>();
        // cryptoWalletArrayList.add(new CryptoWallet("BTC", "btc", 2.13, 1.4, 14021.35));
        // cryptoWalletArrayList.add(new CryptoWallet("ETH", "eth", -1.13, 3.6, 2145.21));
        // cryptoWalletArrayList.add(new CryptoWallet("XRP", "xrp", -3.14, 2.6, 21463.10));
        // cryptoWalletArrayList.add(new CryptoWallet("LTC", "ltc", 4.54, 3.5, 5412.46));
        // adapter = new CryptoWalletAdapter(cryptoWalletArrayList);
        // recyclerView.setAdapter(adapter);
    }

    private void setupProfileIconClick() {
        ImageView profileIcon = findViewById(R.id.loginHome); // Assuming the profile icon is imageView
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    showLogoutDialog();
                } else {
                    navigateToLogin();
                }
            }
        });
    }

    private boolean isUserLoggedIn() {
        return (currentUser != null);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Already Signed In")
                .setMessage("You are already signed in. Do you wish to sign out and log in using a different account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutCurrentUser();
                        navigateToLogin();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void logoutCurrentUser() {
        mAuth.signOut();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void fetchUserData() {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            userListener = db.collection("users").document(uid)
                    .addSnapshotListener(this, (snapshot, e) -> {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            String userName = snapshot.getString("name");
                            String userEmail = snapshot.getString("email");

                            // Update UI with user data
                            userNameTextView.setText(userName);
                            userEmailTextView.setText(userEmail);
                        } else {
                            Log.d(TAG, "Current data: null");

                            userNameTextView.setText("no user name");
                            userEmailTextView.setText("no user email");
                        }
                    });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userListener != null) {
            userListener.remove();
        }
    }
}
