package com.example.trading.application.service;

import com.example.trading.application.dto.BidAsk;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

public class BinanceService implements PriceService{

    @Value("${url.binance}")
    public String baseUrl;

    private WebClient webClient;

    @PostConstruct
    private void initClient() {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public BidAsk getEthUsdT() {
        Object responseObject = executeHttpGet();
        var currencyObject = getCurrencyPairObject(responseObject,"ethusdt");
        return getBidAskFrom(currencyObject);
    }

    @Override
    public BidAsk getBtcUsdT() {
        Object responseObject = executeHttpGet();
        var currencyObject = getCurrencyPairObject(responseObject,"btcusdt");
        return getBidAskFrom(currencyObject);
    }

    private BidAsk getBidAskFrom(LinkedHashMap currencyObject){
        var bid = new BigDecimal((String) currencyObject.get("bidPrice"));
        var ask = new BigDecimal((String) currencyObject.get("askPrice"));
        return new BidAsk(bid,ask);
    }

    private LinkedHashMap getCurrencyPairObject(Object object, String selectedCurrencyPair) {
        var data = (List<LinkedHashMap>) object;
        return data.stream()
                .filter(m -> m.get("symbol").toString().equalsIgnoreCase(selectedCurrencyPair))
                .findFirst().orElse(null);
    }

    private Object executeHttpGet() {
        Object object = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object.class)
                .log()
                .block();
        return object;
    }
}
