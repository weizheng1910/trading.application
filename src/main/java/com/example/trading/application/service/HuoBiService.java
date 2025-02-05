package com.example.trading.application.service;

import com.example.trading.application.dto.BidAsk;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class HuoBiService implements PriceService {

  @Value("${url.huobi}")
  public String baseUrl;

  @Autowired
  private WebClient webClient;

  @Override
  public BidAsk getBidAskOf(String currencyPair) {
    Object responseObject = executeHttpGet();
    var currencyObject = getCurrencyPairObject(responseObject, "ethusdt");
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
            .uri(baseUrl)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Object.class)
            .log()
            .block();
    return object;
  }
}
