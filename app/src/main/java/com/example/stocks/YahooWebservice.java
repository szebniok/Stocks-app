package com.example.stocks;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface YahooWebservice {
    @GET("/market/get-summary")
    public Single<SummaryResult> getSummary(@Header("X-RapidAPI-Key") String key);

    @GET("/market/get-quotes")
    public Single<QuotesResult> getQuotes(@Header("X-RapidAPI-Key") String key, @Query("symbols") String symbols);

    @GET("/market/get-charts")
    public Single<ChartsResult> getCharts(
            @Header("X-RapidAPI-Key") String key,
            @Query("symbol") String symbol,
            @Query("interval") String interval,
            @Query("range") String range
    );

    @GET("/market/auto-complete")
    public Single<AutoCompleteResult> autoComplete(@Header("X-RapidAPI-Key") String key, @Query("query") String query);
}

class SummaryResult {
    public MarketSummaryResponse marketSummaryResponse;

    static class MarketSummaryResponse {
        public List<MarketSummaryResponseStock> result;

        static class MarketSummaryResponseStock {
            public String symbol;
            public String shortName;
            public RegularMarketPrice regularMarketPrice;

            static class RegularMarketPrice {
                public BigDecimal raw;
            }

            public Stock toStock() {
                Stock stock = new Stock();
                stock.setSymbol(symbol);
                stock.setShortName(shortName);
                stock.setRegularMarketPrice(regularMarketPrice.raw);

                return stock;
            }
        }
    }
}

class QuotesResult {
    public QuoteResponse quoteResponse;

    static class QuoteResponse {
        public List<QuotesResultStock> result;

        static class QuotesResultStock {
            public String symbol;
            public String shortName;
            public BigDecimal regularMarketPrice;

            public Stock toStock() {
                Stock stock = new Stock();
                stock.setSymbol(symbol);
                stock.setShortName(shortName);
                stock.setRegularMarketPrice(regularMarketPrice);

                return stock;
            }

        }
    }
}

class ChartsResult {
    public Chart chart;

    static class Chart {
        public List<Result> result;

        static class Result {
            public Indicator indicators;

            static class Indicator {
                public List<IndicatorQuote> quote;

                static class IndicatorQuote {
                    public List<BigDecimal> close;
                }
            }
        }
    }
}

class AutoCompleteResult {
    public ResultSet ResultSet;

    static class ResultSet {
        public List<Result> Result;

        static class Result {
            public String symbol;
            public String name;

            public Stock toStock() {
                Stock stock = new Stock();
                stock.setSymbol(symbol);
                stock.setShortName(name);

                return stock;
            }
        }
    }
}