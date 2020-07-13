package com.example.stocks;

import com.example.stocks.domain.YahooWebservice;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class YahooApiProvider {
    public static final String QUOTES_URL = "https://query1.finance.yahoo.com/v7/finance/";
    public static final String AUTOCOMPLETE_URL = "https://autoc.finance.yahoo.com/autoc";
    public static final String NEWS_URL = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=^GSPC";

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(QUOTES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static final YahooWebservice yahooWebservice = retrofit.create(YahooWebservice.class);
}
