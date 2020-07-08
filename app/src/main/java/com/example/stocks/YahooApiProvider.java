package com.example.stocks;

import com.example.stocks.domain.YahooWebservice;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class YahooApiProvider {
    public static final String YahooApiHost = "https://query1.finance.yahoo.com/v7/finance/";

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(YahooApiHost)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static final YahooWebservice yahooWebservice = retrofit.create(YahooWebservice.class);
}
