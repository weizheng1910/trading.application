package com.example.trading.application.controller;

import com.example.trading.application.calculator.CalculatorFactory;
import com.example.trading.application.domain.Balance;
import com.example.trading.application.domain.Transaction;
import com.example.trading.application.dto.TxnRequest;
import com.example.trading.application.exception.BalanceNotFoundException;
import com.example.trading.application.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@Validated
public class AppController {

  @Autowired CurrencyPairRepositoryFactory currencyPairRepositoryFactory;

  @Autowired TransactionRepository transactionRepository;

  @Autowired BalanceRepository balanceRepository;

  @RequestMapping("/price")
  public @ResponseBody <T extends CurrencyPairRepository> String getBestPrice(
      @RequestParam
          @Pattern(
              regexp = "BtcUsdt|EthUsdt",
              message = "Valid request parameter values are \"BtcUsdt\" or \"EtcUsdt\"")
          String currencyPair) {

    var repo = (T) currencyPairRepositoryFactory.getRepository(currencyPair);
    var pair = repo.findTopByOrderByIdDesc();
    return String.format(
        "Best Bid: %s, Best Ask: %s", pair.getBid().toString(), pair.getAsk().toString());
  }

  @RequestMapping("/balance")
  public @ResponseBody String getLatestBalance(@RequestParam String username) {

    var balance =
        balanceRepository
            .findTopByUsernameOrderByCreateDateTimeDesc(username)
            .orElseThrow(
                () -> new BalanceNotFoundException("Please create new account for : " + username));

    return balance.toString();
  }

  @PostMapping("/transact")
  public @ResponseBody <T extends CurrencyPairRepository> String executeTransaction(
      @Valid @RequestBody TxnRequest txnReq) {

    var balance =
        balanceRepository
            .findTopByUsernameOrderByCreateDateTimeDesc(txnReq.getUsername())
            .orElseThrow(
                () ->
                    new BalanceNotFoundException(
                        "Please create new account for : " + txnReq.getUsername()));

    var repo = (T) currencyPairRepositoryFactory.getRepository(txnReq.getPair());
    var latestBestPrice = repo.findTopByOrderByIdDesc();

    var calculator = CalculatorFactory.getCalculator(balance, txnReq.getPair());
    var output = calculator.returnNewBalanceAndTransaction(txnReq, latestBestPrice);

    balanceRepository.save((Balance) output[0]);
    transactionRepository.save((Transaction) output[1]);

    return "SUCCESS";
  }

  @RequestMapping("/history")
  public @ResponseBody String getTxnHistory(@RequestParam String username) {

    var txns = transactionRepository.findByUsername(username);
    if (CollectionUtils.isEmpty(txns)) {
      return "No transactions found!";
    }
    return txns.stream().map(Transaction::toString).collect(Collectors.joining("\n"));
  }
}
