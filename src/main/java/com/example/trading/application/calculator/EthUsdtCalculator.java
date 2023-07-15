package com.example.trading.application.calculator;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.CurrencyPair;
import com.example.trading.application.domain.Transaction;
import com.example.trading.application.dto.TxnRequest;

import java.math.BigDecimal;

public class EthUsdtCalculator implements BalanceCalculator {

  private Balance balance;

  // TO-DO if balance is null
  public EthUsdtCalculator(Balance balance) {
    this.balance = balance;
  }

  @Override
  public Balance calculateNewBalance(TxnRequest txnRequest, CurrencyPair bidAsk) {

    boolean isBuy = txnRequest.getOrder().equalsIgnoreCase("BUY");

    var rateToUse = isBuy ? bidAsk.getBid() : bidAsk.getAsk();
    var corrAmount = rateToUse.multiply(txnRequest.getAmount());

    if (isBuy) {
      var newEthBalance = balance.getEthAmount().add(txnRequest.getAmount());
      var newUsdBalance = balance.getUsdAmount().subtract(corrAmount);
      if (newUsdBalance.compareTo(BigDecimal.ZERO) < 0) {
        throw new RuntimeException("USD Balance falls lower than zero!");
      }

      return returnNewBalance(newEthBalance, newUsdBalance);
    }

    var newUsdBalance = balance.getUsdAmount().add(corrAmount);
    var newEthBalance = balance.getEthAmount().subtract(txnRequest.getAmount());
    if (newEthBalance.compareTo(BigDecimal.ZERO) < 0) {
      throw new RuntimeException("ETH Balance falls lower than zero!");
    }

    return returnNewBalance(newEthBalance, newUsdBalance);
  }

  @Override
  public Transaction calculateNewTransaction(TxnRequest txnRequest, CurrencyPair bidAsk) {

    boolean isBuy = txnRequest.getOrder().equalsIgnoreCase("BUY");
    var rateToUse = isBuy ? bidAsk.getBid() : bidAsk.getAsk();
    var corrAmount = rateToUse.multiply(txnRequest.getAmount());

    var txn = new Transaction();

    txn.setUsername(txnRequest.getUsername());
    txn.setRate(rateToUse);
    txn.setPair(CurrPair.ETHUSDT);

    txn.setBuyCurrency(isBuy ? "ETH" : "USDT");
    txn.setBuyAmount(isBuy ? txnRequest.getAmount() : corrAmount);

    txn.setSellCurrency(!isBuy ? "ETH" : "USDT");
    txn.setSellAmount(!isBuy ? txnRequest.getAmount(): corrAmount);

    return null;
  }

  private Balance returnNewBalance(BigDecimal newEthBalance, BigDecimal newUsdBalance) {
    var newBalance = new Balance();
    newBalance.setBtcAmount(balance.getBtcAmount());
    newBalance.setEthAmount(newEthBalance);
    newBalance.setUsdAmount(newUsdBalance);
    return newBalance;
  }
}
