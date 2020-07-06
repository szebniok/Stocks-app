package com.example.stocks;

import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Single;

@EBean
public class StockMarketService {
    public Single<List<Stock>> getSummary() {
        return YahooApiProvider.yahooWebservice.getSummary(YahooApiProvider.YahooApiKey)
                .map(result -> result.marketSummaryResponse.result.stream()
                        .map(r -> r.toStock()).collect(Collectors.toList()));
    }

    public Single<Stock> getQuote(String symbol) {
        Single<QuotesResult.QuoteResponse.QuotesResultStock> quoteResponse =
                YahooApiProvider.yahooWebservice.getQuotes(YahooApiProvider.YahooApiKey, symbol)
                        .map(result -> result.quoteResponse.result.get(0));

        Single<ChartsResult.Chart.Result> chartResponse =
                YahooApiProvider.yahooWebservice.getCharts(YahooApiProvider.YahooApiKey, symbol, "5m", "1d")
                        .map(result -> result.chart.result.get(0));

        return quoteResponse.zipWith(chartResponse, (quote, chart) -> {
            Stock stock = quote.toStock();
            stock.setTimestamps(chart.indicators.quote.get(0).close);

            return stock;
        });
    }

    public Single<List<Stock>> autoComplete(String name) {
        return YahooApiProvider.yahooWebservice.autoComplete(YahooApiProvider.YahooApiKey, name)
                .map(result -> result.ResultSet.Result.stream()
                        .map(r -> r.toStock()).collect(Collectors.toList()));
    }
}
