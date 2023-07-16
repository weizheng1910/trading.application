package com.example.trading.application.calculator;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.CurrencyPair;
import com.example.trading.application.domain.Transaction;
import com.example.trading.application.dto.TxnRequest;
import com.example.trading.application.exception.InsufficientBalanceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

  @Test
  public void testEthUsdtCalculator_Buy() {
    // Given
    var txnRequest = new TxnRequest();
    txnRequest.setOrder("BUY");
    txnRequest.setPair(CurrPair.ETHUSDT);
    txnRequest.setAmount(new BigDecimal("1000.00"));
    txnRequest.setUsername("John");

    var latestBestPrice = new CurrencyPair();
    latestBestPrice.setBid(new BigDecimal("1.20"));
    latestBestPrice.setAsk(BigDecimal.ZERO);

    var balance = new Balance();
    balance.setUsdAmount(new BigDecimal("1200.00"));
    balance.setUsername("John");

    var calculator = new EthUsdtCalculator(balance);

    // When
    var result = calculator.returnNewBalanceAndTransaction(txnRequest, latestBestPrice);
    var newBalance = (Balance) result[0];
    var newTransaction = (Transaction) result[1];

    // Then
    assertThat(newBalance.getUsdAmount()).isEqualTo("0.0000");
    assertThat(newBalance.getEthAmount()).isEqualTo("1000.00");
    assertThat(newBalance.getBtcAmount()).isEqualTo("0");
    assertThat(newBalance.getUsername()).isEqualTo("John");

    assertThat(newTransaction.getSellAmount()).isEqualTo("1200.0000");
    assertThat(newTransaction.getSellCurrency()).isEqualTo("USDT");
    assertThat(newTransaction.getBuyAmount()).isEqualTo("1000.00");
    assertThat(newTransaction.getBuyCurrency()).isEqualTo("ETH");
    assertThat(newTransaction.getRate()).isEqualTo("1.20");
    assertThat(newTransaction.getUsername()).isEqualTo("John");
  }

  @Test
  public void testEthUsdtCalculator_Sell() {
    // Given
    var txnRequest = new TxnRequest();
    txnRequest.setOrder("SELL");
    txnRequest.setPair(CurrPair.ETHUSDT);
    txnRequest.setAmount(new BigDecimal("1000.00"));
    txnRequest.setUsername("John");

    var latestBestPrice = new CurrencyPair();
    latestBestPrice.setBid(BigDecimal.ZERO);
    latestBestPrice.setAsk(new BigDecimal("1.20"));

    var balance = new Balance();
    balance.setEthAmount(new BigDecimal("1000.00"));
    balance.setUsername("John");

    var calculator = new EthUsdtCalculator(balance);

    // When
    var result = calculator.returnNewBalanceAndTransaction(txnRequest, latestBestPrice);
    var newBalance = (Balance) result[0];
    var newTransaction = (Transaction) result[1];

    // Then
    assertThat(newBalance.getEthAmount()).isEqualTo("0.00");
    assertThat(newBalance.getUsdAmount()).isEqualTo("1200.0000");
    assertThat(newBalance.getBtcAmount()).isEqualTo("0");
    assertThat(newBalance.getUsername()).isEqualTo("John");

    assertThat(newTransaction.getBuyAmount()).isEqualTo("1200.0000");
    assertThat(newTransaction.getBuyCurrency()).isEqualTo("USDT");
    assertThat(newTransaction.getSellAmount()).isEqualTo("1000.00");
    assertThat(newTransaction.getSellCurrency()).isEqualTo("ETH");
    assertThat(newTransaction.getRate()).isEqualTo("1.20");
    assertThat(newTransaction.getUsername()).isEqualTo("John");
  }

  @Test
  public void testEthUsdtCalculator_Sell_InsufficientBalance() {
    // Given
    var txnRequest = new TxnRequest();
    txnRequest.setOrder("SELL");
    txnRequest.setPair(CurrPair.ETHUSDT);
    txnRequest.setAmount(new BigDecimal("1000.00"));
    txnRequest.setUsername("John");

    var latestBestPrice = new CurrencyPair();
    latestBestPrice.setBid(BigDecimal.ZERO);
    latestBestPrice.setAsk(new BigDecimal("1.20"));

    var balance = new Balance();
    balance.setEthAmount(new BigDecimal("999.00"));
    balance.setUsername("John");

    var calculator = new EthUsdtCalculator(balance);

    // When
    try{
      var result = calculator.returnNewBalanceAndTransaction(txnRequest, latestBestPrice);
      Assertions.fail();
    } catch(InsufficientBalanceException e){
      assertThat(e.getMessage()).contains("lower than zero");
    }

  }

  @Test
  public void testBtcUsdtCalculator_Sell_InsufficientBalance() {
    // Given
    var txnRequest = new TxnRequest();
    txnRequest.setOrder("SELL");
    txnRequest.setPair(CurrPair.BTCUSDT);
    txnRequest.setAmount(new BigDecimal("1000.00"));
    txnRequest.setUsername("John");

    var latestBestPrice = new CurrencyPair();
    latestBestPrice.setBid(BigDecimal.ZERO);
    latestBestPrice.setAsk(new BigDecimal("1.20"));

    var balance = new Balance();
    balance.setBtcAmount(new BigDecimal("999.00"));
    balance.setUsername("John");

    var calculator = new BtcUsdtCalculator(balance);

    // When
    try{
      var result = calculator.returnNewBalanceAndTransaction(txnRequest, latestBestPrice);
      Assertions.fail();
    } catch(InsufficientBalanceException e){
      assertThat(e.getMessage()).contains("lower than zero");
    }

  }

  @Test
  public void testBtcUsdtCalculator_Sell() {
    // Given
    var txnRequest = new TxnRequest();
    txnRequest.setOrder("SELL");
    txnRequest.setPair(CurrPair.BTCUSDT);
    txnRequest.setAmount(new BigDecimal("1000.00"));
    txnRequest.setUsername("John");

    var latestBestPrice = new CurrencyPair();
    latestBestPrice.setBid(BigDecimal.ZERO);
    latestBestPrice.setAsk(new BigDecimal("1.20"));

    var balance = new Balance();
    balance.setBtcAmount(new BigDecimal("1000.00"));
    balance.setUsername("John");

    var calculator = new BtcUsdtCalculator(balance);

    // When
    var result = calculator.returnNewBalanceAndTransaction(txnRequest, latestBestPrice);
    var newBalance = (Balance) result[0];
    var newTransaction = (Transaction) result[1];

    // Then
    assertThat(newBalance.getBtcAmount()).isEqualTo("0.00");
    assertThat(newBalance.getUsdAmount()).isEqualTo("1200.0000");
    assertThat(newBalance.getEthAmount()).isEqualTo("0");
    assertThat(newBalance.getUsername()).isEqualTo("John");

    assertThat(newTransaction.getBuyAmount()).isEqualTo("1200.0000");
    assertThat(newTransaction.getBuyCurrency()).isEqualTo("USDT");
    assertThat(newTransaction.getSellAmount()).isEqualTo("1000.00");
    assertThat(newTransaction.getSellCurrency()).isEqualTo("BTC");
    assertThat(newTransaction.getRate()).isEqualTo("1.20");
    assertThat(newTransaction.getUsername()).isEqualTo("John");
  }

  @Test
  public void testEthUsdtCalculator_InsufficientBalance_Buy() {
    // Given
    var txnRequest = new TxnRequest();
    txnRequest.setOrder("BUY");
    txnRequest.setPair(CurrPair.ETHUSDT);
    txnRequest.setAmount(new BigDecimal("1000.00"));
    txnRequest.setUsername("John");

    var latestBestPrice = new CurrencyPair();
    latestBestPrice.setBid(new BigDecimal("1.20"));
    latestBestPrice.setAsk(BigDecimal.ZERO);

    var balance = new Balance();
    balance.setUsdAmount(new BigDecimal("1100.00"));
    balance.setUsername("John");

    var calculator = new EthUsdtCalculator(balance);

    // When
    try {
      var result = calculator.returnNewBalanceAndTransaction(txnRequest, latestBestPrice);
      Assertions.fail();
    } catch (InsufficientBalanceException e) {
      assertThat(e.getMessage()).contains("falls lower than zero");
    }
  }

  @Test
  public void testBtcUsdtCalculator_Buy() {
    // Given
    var txnRequest = new TxnRequest();
    txnRequest.setOrder("BUY");
    txnRequest.setPair(CurrPair.BTCUSDT);
    txnRequest.setAmount(new BigDecimal("1000.00"));
    txnRequest.setUsername("John");

    var latestBestPrice = new CurrencyPair();
    latestBestPrice.setBid(new BigDecimal("1.20"));
    latestBestPrice.setAsk(BigDecimal.ZERO);

    var balance = new Balance();
    balance.setUsdAmount(new BigDecimal("1200.00"));
    balance.setUsername("John");

    var calculator = new BtcUsdtCalculator(balance);

    // When
    var result = calculator.returnNewBalanceAndTransaction(txnRequest, latestBestPrice);
    var newBalance = (Balance) result[0];
    var newTransaction = (Transaction) result[1];

    // Then
    assertThat(newBalance.getUsdAmount()).isEqualTo("0.0000");
    assertThat(newBalance.getBtcAmount()).isEqualTo("1000.00");
    assertThat(newBalance.getEthAmount()).isEqualTo("0");
    assertThat(newBalance.getUsername()).isEqualTo("John");

    assertThat(newTransaction.getSellAmount()).isEqualTo("1200.0000");
    assertThat(newTransaction.getSellCurrency()).isEqualTo("USDT");
    assertThat(newTransaction.getBuyAmount()).isEqualTo("1000.00");
    assertThat(newTransaction.getBuyCurrency()).isEqualTo("BTC");
    assertThat(newTransaction.getRate()).isEqualTo("1.20");
    assertThat(newTransaction.getUsername()).isEqualTo("John");
  }

  @Test
  public void testBtcUsdtCalculator_InsufficientBalance_Buy() {
    // Given
    var txnRequest = new TxnRequest();
    txnRequest.setOrder("BUY");
    txnRequest.setPair(CurrPair.BTCUSDT);
    txnRequest.setAmount(new BigDecimal("1000.00"));
    txnRequest.setUsername("John");

    var latestBestPrice = new CurrencyPair();
    latestBestPrice.setBid(new BigDecimal("1.20"));
    latestBestPrice.setAsk(BigDecimal.ZERO);

    var balance = new Balance();
    balance.setUsdAmount(new BigDecimal("1100.00"));
    balance.setUsername("John");

    var calculator = new BtcUsdtCalculator(balance);

    // When
    try {
      var result = calculator.returnNewBalanceAndTransaction(txnRequest, latestBestPrice);
      Assertions.fail();
    } catch (InsufficientBalanceException e) {
      assertThat(e.getMessage()).contains("falls lower than zero");
    }
  }
}
