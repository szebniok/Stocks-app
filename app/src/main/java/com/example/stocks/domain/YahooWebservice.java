package com.example.stocks.domain;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface YahooWebservice {
    @GET("quote?symbols=^GSPC,^DJI,^IXIC,^RUT,CL=F,GC=F,SI=F,EURUSD=X,EURPLN=X,IWDA.AS,^VIX,BTC-USD")
    public Single<QuotesResult> getSummary();

    @GET("quote")
    public Single<QuotesResult> getQuotes(@Query("symbols") String symbols);

    @GET("chart/{symbol}")
    public Single<ChartsResult> getCharts(
            @Path("symbol") String symbol,
            @Query("interval") String interval,
            @Query("range") String range
    );

    @GET
    public Single<AutoCompleteResult> autoComplete(@Url String baseUrl, @Query("query") String query, @Query("lang") String lang);
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