package com.example.stocks.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import lombok.Data;

@Data
public class Stock {
    private String symbol;
    private String shortName;
    private BigDecimal regularMarketPrice;
    private List<BigDecimal> timestamps;
    private Boolean favourite;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(symbol, stock.symbol) &&
                Objects.equals(shortName, stock.shortName) &&
                Objects.equals(regularMarketPrice, stock.regularMarketPrice) &&
                Objects.equals(favourite, stock.favourite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, shortName, regularMarketPrice, favourite);
    }
}
