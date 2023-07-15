package com.example.trading.application.service;

import com.example.trading.application.domain.CurrencyPair;
import com.example.trading.application.domain.CurrencyPairFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PriceAggregationService {

    private final BinanceService binanceService;
    private final HuoBiService huoBiService;

    public CurrencyPair aggregateAndStore(String currencyPair){

        var binanceBidAsk = binanceService.getBidAskOf(currencyPair);
        var huoBiBidAsk = huoBiService.getBidAskOf(currencyPair);

        var bestBid = binanceBidAsk.getBid().min(huoBiBidAsk.getBid());
        var bestAsk = binanceBidAsk.getAsk().max(huoBiBidAsk.getAsk());

        var pair = CurrencyPairFactory.getCurrencyPair(currencyPair);
        pair.setBid(bestBid);
        pair.setAsk(bestAsk);

        return pair;
    }

}
