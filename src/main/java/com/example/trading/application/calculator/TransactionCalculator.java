package com.example.trading.application.calculator;

import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.CurrencyPair;
import com.example.trading.application.domain.Transaction;
import com.example.trading.application.dto.TxnRequest;


public interface TransactionCalculator {

    Object[] returnNewBalanceAndTransaction(TxnRequest txnRequest, CurrencyPair bidAsk);
}
