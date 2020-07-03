package com.example.stocks;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import io.reactivex.Single;

public interface YahooWebservice {
    @GET("/market/get-summary")
    public Single<ApiResult> getSummary(@Header("X-RapidAPI-Key") String key);
}

class ApiResult {
    public MarketSummaryResponse marketSummaryResponse;

    static class MarketSummaryResponse {
        public List<Stock> result;
    }
}
