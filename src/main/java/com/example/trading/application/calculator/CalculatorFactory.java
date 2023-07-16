package com.example.trading.application.calculator;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.domain.Balance;

public class CalculatorFactory {

    public static TransactionCalculator getCalculator(Balance balance, String currencyPair){
        return switch(currencyPair){
            case CurrPair.ETHUSDT -> new EthUsdtCalculator(balance);
            // case CurrPair.BTCUSDT -> new BtcUsdtCalculator(balance);
            default -> throw new IllegalStateException("Unexpected value: " + currencyPair);
        };
    }
}
