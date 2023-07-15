package com.example.trading.application.controller;

import com.example.trading.application.calculator.CalculatorFactory;
import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.CurrencyPair;
import com.example.trading.application.dto.TxnRequest;
import com.example.trading.application.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppController {

    @Autowired
    CurrencyPairRepositoryFactory currencyPairRepositoryFactory;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BalanceRepository balanceRepository;


    @RequestMapping("/price")
    public @ResponseBody<T extends CurrencyPairRepository>  String getBestPrice(@RequestParam String currencyPair) {
        // TO-DO request param check
        T repo = (T) currencyPairRepositoryFactory.getRepository(currencyPair);
        var pair = repo.findTopByOrderByIdDesc();
        return String.format("Best Bid: %s, Best Ask: %s", pair.getBid().toString(), pair.getAsk().toString());
    }

    @PostMapping("/transact")
    public @ResponseBody<T extends CurrencyPairRepository> String executeTransaction(@Valid @RequestBody TxnRequest txnReq){

        // Read Input
        Balance balance = new Balance(); // findLatestBalanceByUser; OrElseThrow

        // Calculate Buy Sell
        T repo = (T) currencyPairRepositoryFactory.getRepository(txnReq.getPair());
        var pair = repo.findTopByOrderByIdDesc();

        var calculator = CalculatorFactory.getCalculator(balance, txnReq.getPair());
        var newTransaction = calculator.calculateNewTransaction(txnReq, pair);
        var newBalance = calculator.calculateNewBalance(txnReq, pair);

        transactionRepository.save(newTransaction);
        balanceRepository.save(newBalance);

        return "SUCCESS";
    }




}
