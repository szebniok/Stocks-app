package com.example.stocks;

import org.androidannotations.annotations.EBean;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean
public class StockMarketRepository {
    private YahooWebservice yahooWebservice;
    private static final String YahooApiKey = BuildConfig.YahooAPIKey;

    public StockMarketRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.YahooAPIHost)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        yahooWebservice = retrofit.create(YahooWebservice.class);
    }

    public Single<List<Stock>> getSummary() {
        return yahooWebservice.getSummary(YahooApiKey)
                .map(result -> result.marketSummaryResponse.result);
    }
}
