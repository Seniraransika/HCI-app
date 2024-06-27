package com.example.cryptoapp;

import com.google.gson.annotations.SerializedName;

public class CurrencyRateResponse {
    @SerializedName("currency")
    private String currency;

    @SerializedName("price")
    private double price;

    // Getters and Setters
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
