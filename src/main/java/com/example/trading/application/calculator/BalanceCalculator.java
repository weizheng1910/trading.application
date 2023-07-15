package com.example.trading.application.calculator;

import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.CurrencyPair;
import com.example.trading.application.domain.Transaction;
import com.example.trading.application.dto.TxnRequest;


public interface BalanceCalculator {
    Balance calculateNewBalance(TxnRequest txnRequest, CurrencyPair bidAsk);

    Transaction calculateNewTransaction(TxnRequest txnRequest, CurrencyPair bidAsk);
}
