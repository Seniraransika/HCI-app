package com.example.cryptoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CryptoWalletAdapter extends RecyclerView.Adapter<CryptoWalletAdapter.ViewHolder> {
    private ArrayList<CryptoWallet> cryptoWalletList;

    CryptoWalletAdapter(ArrayList<CryptoWallet> cryptoWalletList) {
        this.cryptoWalletList = cryptoWalletList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crypto_wallet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CryptoWallet cryptoWallet = cryptoWalletList.get(position);
        holder.nameTextView.setText(cryptoWallet.getName());
        holder.symbolTextView.setText(cryptoWallet.getSymbol());
        holder.priceTextView.setText(String.valueOf(cryptoWallet.getPrice()));
        // Bind other data fields as necessary
    }

    @Override
    public int getItemCount() {
        return cryptoWalletList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView symbolTextView;
        TextView priceTextView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cryptoName);
            symbolTextView = itemView.findViewById(R.id.cryptoSymbol);
            priceTextView = itemView.findViewById(R.id.cryptoPrice);
            // Initialize other views as necessary
        }
    }
}
