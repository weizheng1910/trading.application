package com.example.trading.application.calculator;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.CurrencyPair;
import com.example.trading.application.domain.Transaction;
import com.example.trading.application.dto.TxnRequest;
import com.example.trading.application.exception.InsufficientBalanceException;

import java.math.BigDecimal;

public class EthUsdtCalculator implements TransactionCalculator {

  private Balance balance;

  // TO-DO if balance is null
  public EthUsdtCalculator(Balance balance) {
    this.balance = balance;
  }

  Balance calculateNewBalance(TxnRequest txnRequest, CurrencyPair bidAsk) {

    boolean isBuy = txnRequest.getOrder().equalsIgnoreCase("BUY");
    var rateToUse = isBuy ? bidAsk.getBid() : bidAsk.getAsk();
    var corrAmount = rateToUse.multiply(txnRequest.getAmount());

    if (isBuy) {
      var newEthBalance = balance.getEthAmount().add(txnRequest.getAmount());
      var newUsdBalance = balance.getUsdAmount().subtract(corrAmount);
      if (newUsdBalance.compareTo(BigDecimal.ZERO) < 0) {
        throw new InsufficientBalanceException("USD Balance falls lower than zero!");
      }

      return returnNewBalance(newEthBalance, newUsdBalance);
    }

    var newUsdBalance = balance.getUsdAmount().add(corrAmount);
    var newEthBalance = balance.getEthAmount().subtract(txnRequest.getAmount());
    if (newEthBalance.compareTo(BigDecimal.ZERO) < 0) {
      throw new InsufficientBalanceException("ETH Balance falls lower than zero!");
    }

    return returnNewBalance(newEthBalance, newUsdBalance);
  }

  Transaction calculateNewTransaction(TxnRequest txnRequest, CurrencyPair bidAsk) {

    boolean isBuyOrder = txnRequest.getOrder().equalsIgnoreCase("BUY");
    var rateToUse = isBuyOrder ? bidAsk.getBid() : bidAsk.getAsk();
    var corrAmount = rateToUse.multiply(txnRequest.getAmount());

    var txn = new Transaction();

    txn.setUsername(txnRequest.getUsername());
    txn.setRate(rateToUse);
    txn.setPair(CurrPair.ETHUSDT);

    txn.setBuyCurrency(isBuyOrder ? "ETH" : "USDT");
    txn.setBuyAmount(isBuyOrder ? txnRequest.getAmount() : corrAmount);

    txn.setSellCurrency(!isBuyOrder ? "ETH" : "USDT");
    txn.setSellAmount(!isBuyOrder ? txnRequest.getAmount(): corrAmount);

    return txn;
  }

  @Override
  public Object[] returnNewBalanceAndTransaction(TxnRequest txnRequest, CurrencyPair bidAsk){

    var balance = calculateNewBalance(txnRequest, bidAsk);
    var txn = calculateNewTransaction(txnRequest, bidAsk);

    return new Object[]{balance, txn};
  }

  private Balance returnNewBalance(BigDecimal newEthBalance, BigDecimal newUsdBalance) {

    var newBalance = new Balance();
    newBalance.setEthAmount(newEthBalance);
    newBalance.setUsdAmount(newUsdBalance);

    newBalance.setBtcAmount(balance.getBtcAmount());
    newBalance.setUsername(balance.getUsername());
    return newBalance;
  }
}
