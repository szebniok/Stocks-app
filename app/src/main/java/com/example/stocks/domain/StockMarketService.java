package com.example.stocks.domain;

import com.example.stocks.YahooApiProvider;
import com.example.stocks.domain.ChartsResult.Chart.Result;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Single;

@EBean
public class StockMarketService {
    private static final String INTERVAL = "5m";
    private static final String RANGE = "1d";

    public Single<List<Stock>> getSummary() {
        return YahooApiProvider.yahooWebservice
                .getSummary()
                .map(result -> result.quoteResponse.result.stream()
                        .map(r -> r.toStock())
                        .collect(Collectors.toList())
                );
    }

    public Single<Stock> getQuoteAndChart(String symbol) {
        Single<Stock> quote = getQuote(symbol);

        Single<Result> chartResponse = getChart(symbol);

        return quote.zipWith(chartResponse, (stock, chart) -> {
            stock.setTimestamps(chart.indicators.quote.get(0).close);

            return stock;
        });
    }

    public Single<Stock> getQuote(String symbol) {
        return getQuotes(Collections.singletonList(symbol))
                .map(list -> list.get(0));
    }

    public Single<List<Stock>> getQuotes(List<String> symbols) {
        if (symbols.isEmpty()) {
            return Single.just(new ArrayList<>());
        }

        return YahooApiProvider.yahooWebservice
                .getQuotes(String.join(",", symbols))
                .map(result -> result.quoteResponse.result.stream()
                        .map(r -> r.toStock())
                        .collect(Collectors.toList())
                );
    }

    public Single<List<Stock>> autoComplete(String name) {
        return YahooApiProvider.yahooWebservice.autocomplete(YahooApiProvider.AUTOCOMPLETE_URL, name, "en")
                .map(result -> result.ResultSet.Result.stream()
                        .map(r -> r.toStock())
                        .collect(Collectors.toList())
                );
    }

    public Single<List<SyndEntry>> getNews() {

        SyndFeedInput syndFeedInput = new SyndFeedInput();
        return YahooApiProvider.yahooWebservice.getNews(YahooApiProvider.NEWS_URL)
                .map(responseBody -> {
                    SyndFeed feed = syndFeedInput.build(new XmlReader(responseBody.byteStream()));
                    return feed.getEntries();
                });
    }

    private Single<Result> getChart(String symbol) {
        return YahooApiProvider.yahooWebservice.getCharts(symbol, INTERVAL, RANGE)
                .map(result -> result.chart.result.get(0));
    }

}
