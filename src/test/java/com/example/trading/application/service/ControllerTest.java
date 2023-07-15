package com.example.trading.application.service;

import com.example.trading.application.domain.BtcUsdtPair;
import com.example.trading.application.domain.EthUsdtPair;
import com.example.trading.application.dto.TxnRequest;
import com.example.trading.application.repository.BtcUsdtPairRepository;
import com.example.trading.application.repository.CurrencyPairRepositoryFactory;
import com.example.trading.application.repository.EthUsdtPairRepository;

import jakarta.annotation.PostConstruct;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BtcUsdtPairRepository btcUsdtPairRepository;

    @Autowired
    private EthUsdtPairRepository ethUsdtPairRepository;

    @Autowired
    private CurrencyPairRepositoryFactory currencyPairRepositoryFactory;

    String url;
    String postTransactUrl;

    @PostConstruct
    private void setUrl(){
        url =  "http://localhost:" + port;
        postTransactUrl = url + "/transact";
    }

    @Test
    public void getLatestRate() throws Exception {

        populateDataInDb();

        URI uri = UriComponentsBuilder.fromHttpUrl(url).path("/")
                .queryParam("currencyPair", "BtcUsdt").build().toUri();

        assertThat(this.restTemplate.getForObject(uri,
                String.class)).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void testImproperTransactionInputIn() throws JSONException {

        var txnRequest = new TxnRequest();
        txnRequest.setAmount(BigDecimal.TEN);
        txnRequest.setOrder("dsfs");
        txnRequest.setPair("df");

        String response =
                restTemplate.postForObject(postTransactUrl, txnRequest, String.class);

        assertThat(response).contains("Valid values are");

    }

    @Test
    public void testImproperTransactionInputIn_2() throws JSONException {

        var txnRequest = new TxnRequest();
        txnRequest.setAmount(BigDecimal.TEN);
        txnRequest.setOrder(null);
        txnRequest.setPair("BtcUsdt");


        String response =
                restTemplate.postForObject(postTransactUrl, txnRequest, String.class);

        assertThat(response).contains("Valid values are");

    }

    @Test
    public void testProperTransactionInput() throws JSONException {

        var txnRequest = new TxnRequest();
        txnRequest.setAmount(new BigDecimal("1234.12"));
        txnRequest.setPair("BtcUsdt");
        txnRequest.setOrder("BUY");

        String response =
                restTemplate.postForObject(postTransactUrl, txnRequest, String.class);

        assertThat(response).isEqualTo("SUCCESS");

    }

    private void populateDataInDb() {

        var pair = new BtcUsdtPair();
        pair.setAsk(BigDecimal.ONE);
        pair.setBid(BigDecimal.ONE);

        var pair1 = new BtcUsdtPair();
        pair1.setAsk(BigDecimal.TEN);
        pair1.setBid(BigDecimal.TEN);

        var pair2 = new BtcUsdtPair();
        pair2.setAsk(BigDecimal.ZERO);
        pair2.setBid(BigDecimal.ZERO);

        var pairA = new EthUsdtPair();
        pairA.setAsk(BigDecimal.ONE);
        pairA.setBid(BigDecimal.ONE);

        var pairB = new EthUsdtPair();
        pairB.setAsk(BigDecimal.TEN);
        pairB.setBid(BigDecimal.TEN);

        var pairC = new EthUsdtPair();
        pairC.setAsk(new BigDecimal("999"));
        pairC.setBid(new BigDecimal("999"));

        btcUsdtPairRepository.save(pair);
        btcUsdtPairRepository.save(pair1);
        btcUsdtPairRepository.save(pair2);

        ethUsdtPairRepository.save(pairA);
        ethUsdtPairRepository.save(pairB);
        ethUsdtPairRepository.save(pairC);
    }
}
