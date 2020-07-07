package com.example.stocks;

import com.example.stocks.domain.YahooWebservice;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class YahooApiProvider {
    public static final String YahooApiHost = "https://apidojo-yahoo-finance-v1.p.rapidapi.com";
    public static final String YahooApiKey = "yhDyuXUP2HB27vnY5Z1FIaN7zuZMSYBy";

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(YahooApiHost)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static final YahooWebservice yahooWebservice = retrofit.create(YahooWebservice.class);
}
