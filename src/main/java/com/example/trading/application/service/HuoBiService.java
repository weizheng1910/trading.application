package com.example.trading.application.service;

import com.example.trading.application.dto.BidAsk;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class HuoBiService implements PriceService {

  @Value("${url.huobi}")
  public String baseUrl;

  private WebClient webClient;

  @PostConstruct
  private void initClient() {
    webClient = WebClient.builder().baseUrl(baseUrl).build();
  }

  @Override
  public BidAsk getEthUsdT() {
    Object responseObject = executeHttpGet();
    var currencyObject = getCurrencyPairObject(responseObject, "ethusdt");
    return getBidAskFrom(currencyObject);
  }

  @Override
  public BidAsk getBtcUsdT() {
    Object responseObject = executeHttpGet();
    var currencyObject = getCurrencyPairObject(responseObject, "btcusdt");
    return getBidAskFrom(currencyObject);
  }

  private BidAsk getBidAskFrom(LinkedHashMap currencyObject) {
    var bid = new BigDecimal((Double) currencyObject.get("bid"));
    var ask = new BigDecimal((Double) currencyObject.get("ask"));
    return new BidAsk(bid, ask);
  }

  private LinkedHashMap getCurrencyPairObject(Object object, String selectedCurrencyPair) {

    var map = (LinkedHashMap<String, List<LinkedHashMap>>) object;
    var data = map.get("data");
    return data.stream()
        .filter(m -> m.get("symbol").toString().equalsIgnoreCase(selectedCurrencyPair))
        .findFirst()
        .orElse(null);
  }

  private Object executeHttpGet() {
    Object object =
        webClient
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
