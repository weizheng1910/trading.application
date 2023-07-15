package com.example.trading.application.domain;

import com.example.trading.application.constants.CurrPair;

public class CurrencyPairFactory {

    public static CurrencyPair getCurrencyPair(String currencyPair){
        return switch(currencyPair){
            case CurrPair.ETHUSDT -> new EthUsdtPair();
            case CurrPair.BTCUSDT -> new BtcUsdtPair();
            default -> null;
        };
    }
}

