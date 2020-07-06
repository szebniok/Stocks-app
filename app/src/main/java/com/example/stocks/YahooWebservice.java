package com.example.stocks;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import io.reactivex.Single;
import retrofit2.http.Query;

public interface YahooWebservice {
    @GET("/market/get-summary")
    public Single<SummaryResult> getSummary(@Header("X-RapidAPI-Key") String key);

    @GET("/market/get-quotes")
    public Single<QuotesResult> getQuotes(@Header("X-RapidAPI-Key") String key, @Query("symbols") String symbols);
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
        public List<Stock> result;
    }
}
