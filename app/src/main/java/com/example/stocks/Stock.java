package com.example.stocks;

import java.math.BigDecimal;
import java.util.List;

public class Stock {
    private String symbol;
    private String shortName;
    private BigDecimal regularMarketPrice;
    private List<BigDecimal> timestamps;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public BigDecimal getRegularMarketPrice() {
        return regularMarketPrice;
    }

    public void setRegularMarketPrice(BigDecimal regularMarketPrice) {
        this.regularMarketPrice = regularMarketPrice;
    }

    public List<BigDecimal> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<BigDecimal> timestamps) {
        this.timestamps = timestamps;
    }
}
