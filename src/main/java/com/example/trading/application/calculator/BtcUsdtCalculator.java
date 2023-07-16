package com.example.trading.application.calculator;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.CurrencyPair;
import com.example.trading.application.domain.Transaction;
import com.example.trading.application.dto.TxnRequest;
import com.example.trading.application.exception.InsufficientBalanceException;

import java.math.BigDecimal;

public class BtcUsdtCalculator implements TransactionCalculator {

    private Balance balance;

    public BtcUsdtCalculator(Balance balance){
        this.balance = balance;
    }

    public Balance calculateNewBalance(TxnRequest txnRequest, CurrencyPair bidAsk) {
        boolean isBuyOrder = txnRequest.getOrder().equalsIgnoreCase("BUY");
        var rateToUse = isBuyOrder ? bidAsk.getBid() : bidAsk.getAsk();
        var alternativeCurrencyAmt = rateToUse.multiply(txnRequest.getAmount());

        if (isBuyOrder) {
            var newBtcBalance = balance.getBtcAmount().add(txnRequest.getAmount());
            var newUsdBalance = balance.getUsdAmount().subtract(alternativeCurrencyAmt);
            if (newUsdBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("USD Balance falls lower than zero!");
            }

            return returnNewBalance(newBtcBalance, newUsdBalance);
        }

        var newUsdBalance = balance.getUsdAmount().add(alternativeCurrencyAmt);
        var newBtcBalance = balance.getBtcAmount().subtract(txnRequest.getAmount());
        if (newBtcBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("BTC Balance falls lower than zero!");
        }

        return returnNewBalance(newBtcBalance, newUsdBalance);
    }

    Transaction calculateNewTransaction(TxnRequest txnRequest, CurrencyPair bidAsk) {

        boolean isBuyOrder = txnRequest.getOrder().equalsIgnoreCase("BUY");
        var rateToUse = isBuyOrder ? bidAsk.getBid() : bidAsk.getAsk();
        var corrAmount = rateToUse.multiply(txnRequest.getAmount());

        var txn = new Transaction();

        txn.setUsername(txnRequest.getUsername());
        txn.setRate(rateToUse);
        txn.setPair(CurrPair.BTCUSDT);

        txn.setBuyCurrency(isBuyOrder ? "BTC" : "USDT");
        txn.setBuyAmount(isBuyOrder ? txnRequest.getAmount() : corrAmount);

        txn.setSellCurrency(!isBuyOrder ? "BTC" : "USDT");
        txn.setSellAmount(!isBuyOrder ? txnRequest.getAmount(): corrAmount);

        return txn;
    }

    @Override
    public Object[] returnNewBalanceAndTransaction(TxnRequest txnRequest, CurrencyPair bidAsk){

        var balance = calculateNewBalance(txnRequest, bidAsk);
        var txn = calculateNewTransaction(txnRequest, bidAsk);

        return new Object[]{balance, txn};
    }

    private Balance returnNewBalance(BigDecimal newBtcAmount, BigDecimal newUsdBalance) {

        var newBalance = new Balance();
        newBalance.setBtcAmount(newBtcAmount);
        newBalance.setUsdAmount(newUsdBalance);

        newBalance.setEthAmount(balance.getEthAmount());
        newBalance.setUsername(balance.getUsername());
        return newBalance;
    }
}
