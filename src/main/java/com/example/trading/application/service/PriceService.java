package com.example.trading.application.service;

import com.example.trading.application.dto.BidAsk;

public interface PriceService {

    BidAsk getEthUsdT();

    BidAsk getBtcUsdT();
}
