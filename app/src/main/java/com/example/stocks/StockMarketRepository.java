package com.example.stocks;

import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean
public class StockMarketRepository {
    private YahooWebservice yahooWebservice;
    private static final String YahooApiKey = BuildConfig.YahooAPIKey;

    public StockMarketRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.YahooAPIHost)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        yahooWebservice = retrofit.create(YahooWebservice.class);
    }

    public Single<List<Stock>> getSummary() {
        return yahooWebservice.getSummary(YahooApiKey)
                .map(result -> result.marketSummaryResponse.result.stream()
                        .map(r -> r.toStock()).collect(Collectors.toList()));
    }

    public Single<Stock> getQuote(String symbol) {
        Single<QuotesResult.QuoteResponse.QuotesResultStock> quoteResponse =
                yahooWebservice.getQuotes(YahooApiKey, symbol)
                        .map(result -> result.quoteResponse.result.get(0));

        Single<ChartsResult.Chart.Result> chartResponse =
                yahooWebservice.getCharts(YahooApiKey, symbol, "5m", "1d")
                    .map(result -> result.chart.result.get(0));

        return quoteResponse.zipWith(chartResponse, (quote, chart) -> {
            Stock stock = quote.toStock();
            stock.setTimestamps(chart.indicators.quote.get(0).close);

            return stock;
        });
    }
}
