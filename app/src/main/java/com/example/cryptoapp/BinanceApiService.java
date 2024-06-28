package com.example.cryptoapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BinanceApiService {
    @GET("/api/v3/ticker/price")
    Call<CryptoPrice> getTickerPrice(@Query("symbol") String symbol);
}
