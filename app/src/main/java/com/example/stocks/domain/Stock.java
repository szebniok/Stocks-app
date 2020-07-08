package com.example.stocks.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class Stock {
    private String symbol;
    private String shortName;
    private BigDecimal regularMarketPrice;
    private List<BigDecimal> timestamps;
}
