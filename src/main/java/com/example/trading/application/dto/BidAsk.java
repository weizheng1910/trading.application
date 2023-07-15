package com.example.trading.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class BidAsk {

    private BigDecimal bid;
    private BigDecimal ask;
}
