package com.example.cryptoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private TextView userNameTextView, userEmailTextView;
    private ImageView userImageView;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

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
        userImageView = findViewById(R.id.userView);

        // Fetch user data
        fetchUserData();

        // Initialize RecyclerView
        recyclerViewInit();

        // Set up buttons
        setupButtons();
    }

    private void fetchUserData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("users").document(userId);
            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        String userName = snapshot.getString("name");
                        String userEmail = snapshot.getString("email");
                        String userProfilePic = snapshot.getString("profilePicUrl");

                        userNameTextView.setText(userName);
                        userEmailTextView.setText(userEmail);
                        if (userProfilePic != null && !userProfilePic.isEmpty()) {
                            Glide.with(MainActivity.this)
                                    .load(userProfilePic)
                                    .into(userImageView);
                        }
                    }
                }
            });
        }
    }

    private void recyclerViewInit() {
        recyclerView = findViewById(R.id.view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Sample data
        ArrayList<CryptoWallet> cryptoWalletArrayList = new ArrayList<>();
        cryptoWalletArrayList.add(new CryptoWallet("BTC", "btc", 2.13, 1.4, 14021.35));
        cryptoWalletArrayList.add(new CryptoWallet("ETH", "eth", -1.13, 3.6, 2145.21));
        cryptoWalletArrayList.add(new CryptoWallet("XRP", "xrp", -3.14, 2.6, 21463.10));
        cryptoWalletArrayList.add(new CryptoWallet("LTC", "ltc", 4.54, 3.5, 5412.46));

        adapter = new CryptoWalletAdapter(cryptoWalletArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void setupButtons() {
        AppCompatButton cryptoValueButton = findViewById(R.id.cryptoValue);
        cryptoValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CurrencyRateView.class);
                startActivity(intent);
            }
        });
    }

    // Sample CryptoWallet class and Adapter
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

    class CryptoWalletAdapter extends RecyclerView.Adapter<CryptoWalletAdapter.ViewHolder> {
        private ArrayList<CryptoWallet> cryptoWalletList;

        CryptoWalletAdapter(ArrayList<CryptoWallet> cryptoWalletList) {
            this.cryptoWalletList = cryptoWalletList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.crypto_item, parent, false);
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
