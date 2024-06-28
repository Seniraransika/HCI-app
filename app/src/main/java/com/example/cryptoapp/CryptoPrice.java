package com.example.cryptoapp;

import com.google.gson.annotations.SerializedName;

public class CryptoPrice {
    @SerializedName("symbol")
    private String symbol;

    @SerializedName("price")
    private String price;

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
