package com.example.cryptoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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

    private ImageView userImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Find the button by its ID
        AppCompatButton cryptoValueButton = findViewById(R.id.cryptoValue);
        // Set an OnClickListener on the button
        cryptoValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the CurrencyRateView activity
                Intent intent = new Intent(MainActivity.this, CurrencyRateView.class);
                startActivity(intent);
            }
        });
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize Views
        userNameTextView = findViewById(R.id.userNameview);
        userEmailTextView = findViewById(R.id.userEmailview);
        userImageView = findViewById(R.id.userView); // Initialize userImageView

        recyclerViewInit();
        setupProfileIconClick();
        fetchUserData();
    }

    private void recyclerViewInit() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.view);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Uncomment and complete the following lines if you have the necessary classes
         ArrayList<CryptoWallet> cryptoWalletArrayList = new ArrayList<>();
         cryptoWalletArrayList.add(new CryptoWallet("BTC", "btc", 2.13, 1.4, 14021.35));
         cryptoWalletArrayList.add(new CryptoWallet("ETH", "eth", -1.13, 3.6, 2145.21));
         cryptoWalletArrayList.add(new CryptoWallet("XRP", "xrp", -3.14, 2.6, 21463.10));
         cryptoWalletArrayList.add(new CryptoWallet("LTC", "ltc", 4.54, 3.5, 5412.46));
         adapter = new RecyclerView.Adapter() {
             @NonNull
             @Override
             public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 return null;
             }

             @Override
             public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

             }

             @Override
             public int getItemCount() {
                 return 0;
             }
         };
         recyclerView.setAdapter(adapter);
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
                            String profileImageURL = snapshot.getString("profileImageUrl");

                            // Update UI with user data
                            userNameTextView.setText(userName != null ? userName : "No name");
                            userEmailTextView.setText(userEmail != null ? userEmail : "No email");

                            // Load profile image using Glide
                            if (profileImageURL!=null) {
                                Glide.with(MainActivity.this)
                                        .load(profileImageURL)
//                                        .placeholder(R.drawable.baseline_person_24)
//                                        .error(R.drawable.baseline_person_24)
                                        .into(userImageView);
                            } else {
                                userImageView.setImageResource(R.drawable.baseline_person_24);
                            }
                        } else {
                            Log.d(TAG, "Current data: null");

                            userNameTextView.setText("No user data");
                            userEmailTextView.setText("No user data");
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
