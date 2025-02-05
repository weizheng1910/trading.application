package com.example.trading.application.service;

import com.example.trading.application.dto.BidAsk;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class BinanceService implements PriceService{

    @Value("${url.binance}")
    public String baseUrl;
    @Autowired
    private WebClient webClient;

    @Override
    public BidAsk getBidAskOf(String currencyPair) {
        // TODO: If currency pair is not found!
        Object responseObject = executeHttpGet();
        var currencyObject = getCurrencyPairObject(responseObject,currencyPair);
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
                .uri(baseUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object.class)
                .log()
                .block();
        return object;
    }
}
