package com.example.trading.application.repository;

import com.example.trading.application.domain.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void testOnSave(){

        String username = "John";
        String currency = "USD";
        BigDecimal buyAmount = BigDecimal.ONE;
        String pair = "USDETH";
        String sellCurrency = "ETH";
        BigDecimal sellAmount = new BigDecimal("0.5");
        BigDecimal rate = new BigDecimal("0.5");

        var transaction  = new Transaction();
        transaction.setUsername(username);
        transaction.setBuyCurrency(currency);
        transaction.setBuyAmount(buyAmount);

        transaction.setPair(pair);

        // Bid Or Ask
        transaction.setRate(rate);

        transaction.setSellCurrency(sellCurrency);
        transaction.setSellAmount(sellAmount);

        transactionRepository.save(transaction);

        var persistedTxn = transactionRepository.findAll().get(0);


        assertThat(persistedTxn.getId()).isEqualTo(1L);
        assertThat(persistedTxn.getUsername()).isEqualTo(username);
        assertThat(persistedTxn.getBuyCurrency()).isEqualTo(currency);
        assertThat(persistedTxn.getBuyAmount()).isEqualTo(buyAmount);
        assertThat(persistedTxn.getPair()).isEqualTo(pair);
        assertThat(persistedTxn.getRate()).isEqualTo(rate);
        assertThat(persistedTxn.getSellCurrency()).isEqualTo(sellCurrency);
        assertThat(persistedTxn.getSellAmount()).isEqualTo(sellAmount);
        assertThat(persistedTxn.getCreateDateTime()).isNotNull();


    }
}
