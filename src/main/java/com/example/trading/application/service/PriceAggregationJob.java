package com.example.trading.application.service;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.domain.BtcUsdtPair;
import com.example.trading.application.domain.EthUsdtPair;
import com.example.trading.application.repository.BtcUsdtPairRepository;
import com.example.trading.application.repository.EthUsdtPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PriceAggregationJob {

    @Autowired
    PriceAggregationService priceAggregationService;

    @Autowired
    BtcUsdtPairRepository btcUsdtPairRepository;

    @Autowired
    EthUsdtPairRepository ethUsdtPairRepository;

    @Scheduled(fixedRate = 10000)
    public void executeJob(){

        var ethUsdtPair = (EthUsdtPair) priceAggregationService.aggregateAndStore(CurrPair.ETHUSDT);
        var btcUsdtPair = (BtcUsdtPair) priceAggregationService.aggregateAndStore(CurrPair.BTCUSDT);

        ethUsdtPairRepository.save(ethUsdtPair);
        btcUsdtPairRepository.save(btcUsdtPair);
    }
}
