package com.example.cryptoapp;

import java.util.ArrayList;

public class CryptoWallet {
    private String name;
    private String symbol;
    private double change;
    private double volume;
    private double price;

    public CryptoWallet(String name, String symbol, double change, double volume, double price) {
        this.name = name;
        this.symbol = symbol;
        this.change = change;
        this.volume = volume;
        this.price = price;
    }

    public CryptoWallet(ArrayList<CryptoWallet> cryptoWalletArrayList) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
