package com.example.trading.application.service;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.BtcUsdtPair;
import com.example.trading.application.domain.EthUsdtPair;
import com.example.trading.application.domain.Transaction;
import com.example.trading.application.dto.TxnRequest;
import com.example.trading.application.repository.*;

import jakarta.annotation.PostConstruct;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ControllerTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private BtcUsdtPairRepository btcUsdtPairRepository;

  @Autowired private EthUsdtPairRepository ethUsdtPairRepository;

  @Autowired private CurrencyPairRepositoryFactory currencyPairRepositoryFactory;

  @Autowired private BalanceRepository balanceRepository;

  @Autowired private TransactionRepository transactionRepository;

  String url;
  String postTransactUrl;

  String getTransactHistoryUrl;

  @PostConstruct
  private void setUrl() {
    url = "http://localhost:" + port;
    postTransactUrl = url + "/transact";
    getTransactHistoryUrl = url + "/history";
  }

  @BeforeEach
  public void setUp() {
    populateBalanceInDb();
    populateDataInDb();
  }

  @Test
  public void getUserLatestBalance_UserNotFound() throws Exception {

    URI uri =
        UriComponentsBuilder.fromHttpUrl(url)
            .path("/balance")
            .queryParam("username", "Jacelyn")
            .build()
            .toUri();

    assertThat(this.restTemplate.getForObject(uri, String.class))
        .isEqualTo("Please create new account for : Jacelyn");
  }

  @Test
  public void getUserLatestBalance() throws Exception {

    URI uri =
        UriComponentsBuilder.fromHttpUrl(url)
            .path("/balance")
            .queryParam("username", "John")
            .build()
            .toUri();

    assertThat(this.restTemplate.getForObject(uri, String.class))
        .isEqualTo("Balance - User: John, USD: 10.00, ETH: 10.00, BTC: 10.00");
  }

  @Test
  public void getLatestRate() throws Exception {

    URI uri =
        UriComponentsBuilder.fromHttpUrl(url)
            .path("/price")
            .queryParam("currencyPair", "BtcUsdt")
            .build()
            .toUri();

    assertThat(this.restTemplate.getForObject(uri, String.class))
        .isEqualTo("Best Bid: 0.00, Best Ask: 0.00");
  }

  @Test
  public void getLatestRate_ImproperInput() throws Exception {

    URI uri =
        UriComponentsBuilder.fromHttpUrl(url)
            .path("/price")
            .queryParam("currencyPair", "f6gkr")
            .build()
            .toUri();

    assertThat(this.restTemplate.getForObject(uri, String.class))
        .isEqualTo(" Valid request parameter values are \"BtcUsdt\" or \"EtcUsdt\"");
  }

  @Test
  public void testImproperTransactionInputIn() throws JSONException {

    var txnRequest = new TxnRequest();
    txnRequest.setAmount(BigDecimal.TEN);
    txnRequest.setOrder("dsfs");
    txnRequest.setPair("df");

    String response = restTemplate.postForObject(postTransactUrl, txnRequest, String.class);

    assertThat(response).contains("Valid values are");
  }

  @Test
  public void testImproperTransactionInputIn_2() throws JSONException {

    var txnRequest = new TxnRequest();
    txnRequest.setAmount(BigDecimal.TEN);
    txnRequest.setOrder(null);
    txnRequest.setPair("BtcUsdt");

    String response = restTemplate.postForObject(postTransactUrl, txnRequest, String.class);

    assertThat(response).contains("Valid values are");
  }

  @Test
  public void testProperTransactionInput() throws JSONException {

    var txnRequest = new TxnRequest();
    txnRequest.setAmount(new BigDecimal("1234.12"));
    txnRequest.setPair("BtcUsdt");
    txnRequest.setOrder("BUY");
    txnRequest.setUsername("John");

    String response = restTemplate.postForObject(postTransactUrl, txnRequest, String.class);

    assertThat(response).isEqualTo("SUCCESS");
  }

  @Test
  public void testTransactionHistory() {

    populateTransactionInDb();

    URI uri =
        UriComponentsBuilder.fromHttpUrl(url)
            .path("/history")
            .queryParam("username", "John")
            .build()
            .toUri();

    assertThat(this.restTemplate.getForObject(uri, String.class))
        .contains(
            "Transaction - id: 1, buyCurrency: ETH, buyAmt: 1.00, sellCurrency: USD, sellAmt: 10.00, pair: EthUsdt, rate: 1.00, user: John");
  }

  @Test
  public void testTransactionHistory_Empty() {

    populateTransactionInDb();

    URI uri =
        UriComponentsBuilder.fromHttpUrl(url)
            .path("/history")
            .queryParam("username", "Beckham")
            .build()
            .toUri();

    assertThat(this.restTemplate.getForObject(uri, String.class))
        .contains("No transactions found!");
  }

  private void populateBalanceInDb() {
    var balance = new Balance();
    balance.setUsername("Peter");
    balance.setUsdAmount(BigDecimal.ZERO);
    balance.setEthAmount(BigDecimal.ZERO);
    balance.setBtcAmount(BigDecimal.ZERO);

    var balance1 = new Balance();
    balance1.setUsername("Peter");
    balance1.setUsdAmount(BigDecimal.ONE);
    balance1.setEthAmount(BigDecimal.ONE);
    balance1.setBtcAmount(BigDecimal.ONE);

    var balance2 = new Balance();
    balance2.setUsername("John");
    balance2.setUsdAmount(BigDecimal.ZERO);
    balance2.setEthAmount(BigDecimal.ZERO);
    balance2.setBtcAmount(BigDecimal.ZERO);

    var balance3 = new Balance();
    balance3.setUsername("John");
    balance3.setUsdAmount(BigDecimal.TEN);
    balance3.setEthAmount(BigDecimal.TEN);
    balance3.setBtcAmount(BigDecimal.TEN);

    balanceRepository.save(balance);
    balanceRepository.save(balance1);
    balanceRepository.save(balance2);
    balanceRepository.save(balance3);
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

  private void populateTransactionInDb() {

    var transaction = new Transaction();
    transaction.setUsername("John");
    transaction.setBuyCurrency("ETH");
    transaction.setBuyAmount(BigDecimal.ONE);
    transaction.setPair(CurrPair.ETHUSDT);
    transaction.setRate(BigDecimal.ONE);
    transaction.setSellCurrency("USD");
    transaction.setSellAmount(BigDecimal.TEN);
    transaction.setCreateDateTime(LocalDateTime.MAX);
    transactionRepository.save(transaction);

    var transaction1 = new Transaction();
    transaction1.setUsername("John");
    transaction1.setBuyCurrency("ETH");
    transaction1.setBuyAmount(BigDecimal.ONE);
    transaction1.setPair(CurrPair.ETHUSDT);
    transaction1.setRate(BigDecimal.ONE);
    transaction1.setSellCurrency("USD");
    transaction1.setSellAmount(BigDecimal.TEN);
    transaction1.setCreateDateTime(LocalDateTime.MAX);
    transactionRepository.save(transaction1);
  }
}
