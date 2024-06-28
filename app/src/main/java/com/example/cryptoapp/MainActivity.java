package com.example.cryptoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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

        // Sample data
        ArrayList<CryptoWallet> cryptoWalletArrayList = new ArrayList<>();
        cryptoWalletArrayList.add(new CryptoWallet("BTC", "btc", 2.13, 1.4, 14021.35));
        cryptoWalletArrayList.add(new CryptoWallet("ETH", "eth", -1.13, 3.6, 2145.21));
        cryptoWalletArrayList.add(new CryptoWallet("XRP", "xrp", -3.14, 2.6, 21463.10));
        cryptoWalletArrayList.add(new CryptoWallet("LTC", "ltc", 4.54, 3.5, 5412.46));
        cryptoWalletArrayList.add(new CryptoWallet("ADA", "ada", 1.23, 2.8, 1.34));
        cryptoWalletArrayList.add(new CryptoWallet("DOT", "dot", -0.75, 4.1, 15.72));
        cryptoWalletArrayList.add(new CryptoWallet("LINK", "link", 2.45, 1.9, 18.36));
        cryptoWalletArrayList.add(new CryptoWallet("BNB", "bnb", 3.67, 3.2, 324.57));
        cryptoWalletArrayList.add(new CryptoWallet("DOGE", "doge", -2.56, 2.5, 0.255));
        cryptoWalletArrayList.add(new CryptoWallet("SOL", "sol", 5.89, 4.5, 32.18));
        cryptoWalletArrayList.add(new CryptoWallet("UNI", "uni", 0.92, 2.3, 19.84));
        cryptoWalletArrayList.add(new CryptoWallet("MATIC", "matic", 3.14, 3.8, 1.25));
        cryptoWalletArrayList.add(new CryptoWallet("ICP", "icp", -4.32, 2.1, 34.76));
        cryptoWalletArrayList.add(new CryptoWallet("VET", "vet", 1.87, 1.7, 0.127));
        cryptoWalletArrayList.add(new CryptoWallet("EOS", "eos", 0.51, 2.4, 3.89));
        cryptoWalletArrayList.add(new CryptoWallet("XLM", "xlm", -1.05, 2.9, 0.365));
        cryptoWalletArrayList.add(new CryptoWallet("TRX", "trx", 1.98, 2.2, 0.078));
        cryptoWalletArrayList.add(new CryptoWallet("ATOM", "atom", 2.76, 3.3, 8.94));
        cryptoWalletArrayList.add(new CryptoWallet("XTZ", "xtz", 0.83, 2.6, 2.97));
        cryptoWalletArrayList.add(new CryptoWallet("AAVE", "aave", 3.82, 3.7, 215.63));
        cryptoWalletArrayList.add(new CryptoWallet("CAKE", "cake", 5.67, 4.2, 13.84));
        cryptoWalletArrayList.add(new CryptoWallet("ALGO", "algo", 0.92, 2.1, 0.88));
        cryptoWalletArrayList.add(new CryptoWallet("FTT", "ftt", 4.56, 3.9, 28.74));
        cryptoWalletArrayList.add(new CryptoWallet("FIL", "fil", -2.34, 2.3, 51.32));
        cryptoWalletArrayList.add(new CryptoWallet("AVAX", "avax", 6.45, 4.5, 11.87));
        cryptoWalletArrayList.add(new CryptoWallet("ZEC", "zec", -1.78, 2.4, 108.29));
        cryptoWalletArrayList.add(new CryptoWallet("HBAR", "hbar", 1.34, 2.2, 0.183));
        cryptoWalletArrayList.add(new CryptoWallet("WAVES", "waves", 2.91, 3.1, 13.56));
        cryptoWalletArrayList.add(new CryptoWallet("NEO", "neo", 0.76, 2.0, 38.29));
        cryptoWalletArrayList.add(new CryptoWallet("DASH", "dash", 0.45, 2.5, 157.84));
        cryptoWalletArrayList.add(new CryptoWallet("XMR", "xmr", -0.92, 2.8, 232.17));
        cryptoWalletArrayList.add(new CryptoWallet("CRO", "cro", 1.87, 2.6, 0.135));
        cryptoWalletArrayList.add(new CryptoWallet("KSM", "ksm", 3.45, 3.4, 314.52));
        cryptoWalletArrayList.add(new CryptoWallet("GRT", "grt", 2.15, 2.7, 0.654));
        cryptoWalletArrayList.add(new CryptoWallet("IOST", "iost", 0.63, 2.3, 0.017));
        cryptoWalletArrayList.add(new CryptoWallet("REN", "ren", -1.29, 2.9, 0.345));
        cryptoWalletArrayList.add(new CryptoWallet("RUNE", "rune", 4.87, 3.8, 4.32));
        cryptoWalletArrayList.add(new CryptoWallet("UMA", "uma", 5.32, 4.0, 10.78));
        cryptoWalletArrayList.add(new CryptoWallet("LUNA", "luna", 6.54, 4.3, 6.92));

        adapter = new CryptoWalletAdapter(cryptoWalletArrayList);
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
                            if (profileImageURL != null) {
                                Glide.with(MainActivity.this)
                                        .load(profileImageURL)
                                        .placeholder(R.drawable.baseline_person_24)
                                        .error(R.drawable.baseline_person_24)
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

    // Sample CryptoWallet class
    class CryptoWallet {
        String name;
        String symbol;
        double change;
        double volume;
        double price;

        public CryptoWallet(String name, String symbol, double change, double volume, double price) {
            this.name = name;
            this.symbol = symbol;
            this.change = change;
            this.volume = volume;
            this.price = price;
        }

        // Getters
        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }

        public double getChange() {
            return change;
        }

        public double getVolume() {
            return volume;
        }

        public double getPrice() {
            return price;
        }
    }

    // Adapter for RecyclerView
    class CryptoWalletAdapter extends RecyclerView.Adapter<CryptoWalletAdapter.ViewHolder> {
        private ArrayList<CryptoWallet> cryptoWalletList;

        CryptoWalletAdapter(ArrayList<CryptoWallet> cryptoWalletList) {
            this.cryptoWalletList = cryptoWalletList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CryptoWallet currentItem = cryptoWalletList.get(position);

            holder.nameTextView.setText(currentItem.getName());
            holder.symbolTextView.setText(currentItem.getSymbol());
            holder.changeTextView.setText(String.valueOf(currentItem.getChange()));
            holder.volumeTextView.setText(String.valueOf(currentItem.getVolume()));
            holder.priceTextView.setText(String.valueOf(currentItem.getPrice()));
        }

        @Override
        public int getItemCount() {
            return cryptoWalletList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView;
            TextView symbolTextView;
            TextView changeTextView;
            TextView volumeTextView;
            TextView priceTextView;

            ViewHolder(View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.crypto_name);
                symbolTextView = itemView.findViewById(R.id.crypto_symbol);
                changeTextView = itemView.findViewById(R.id.crypto_change);
                volumeTextView = itemView.findViewById(R.id.crypto_volume);
                priceTextView = itemView.findViewById(R.id.crypto_price);
            }
        }
    }
}
