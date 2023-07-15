package com.example.trading.application.repository;

import com.example.trading.application.domain.Balance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Test
    public void testOnSave(){

        BigDecimal usd = BigDecimal.ONE;
        BigDecimal eth = BigDecimal.ONE;
        BigDecimal btc = BigDecimal.ONE;
        String username = "John";

        var balance = new Balance();
        balance.setUsername(username);
        balance.setUsdAmount(usd);
        balance.setEthAmount(eth);
        balance.setBtcAmount(btc);

        balanceRepository.save(balance);

        var persistedBalance = balanceRepository.findAll().get(0);
        assertThat(persistedBalance.getId()).isEqualTo(1L);
        assertThat(persistedBalance.getUsername()).isEqualTo(username);
        assertThat(persistedBalance.getUsdAmount()).isEqualTo(usd);
        assertThat(persistedBalance.getBtcAmount()).isEqualTo(btc);
        assertThat(persistedBalance.getEthAmount()).isEqualTo(eth);

    }
}
