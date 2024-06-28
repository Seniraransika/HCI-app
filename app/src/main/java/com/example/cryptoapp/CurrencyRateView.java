package com.example.cryptoapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyRateView extends AppCompatActivity {

    private TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_rate_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        priceTextView = findViewById(R.id.priceTextView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.binance.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BinanceApiService apiService = retrofit.create(BinanceApiService.class);

        apiService.getTickerPrice("BTCUSDT").enqueue(new Callback<CryptoPrice>() {
            @Override
            public void onResponse(Call<CryptoPrice> call, Response<CryptoPrice> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CryptoPrice price = response.body();
                    priceTextView.setText("BTC/USDT: " + price.getPrice());
                }
            }

            @Override
            public void onFailure(Call<CryptoPrice> call, Throwable t) {
                Log.e("CurrencyRateView", "Error fetching price", t);
            }
        });
    }
}
