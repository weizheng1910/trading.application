package com.example.trading.application.repository;

import com.example.trading.application.domain.Balance;
import com.example.trading.application.exception.BalanceNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository balanceRepository;



    @Test
    @Order(1)
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
        assertThat(persistedBalance.getId()).isGreaterThanOrEqualTo(1L);
        assertThat(persistedBalance.getUsername()).isEqualTo(username);
        assertThat(persistedBalance.getUsdAmount()).isEqualTo(usd);
        assertThat(persistedBalance.getBtcAmount()).isEqualTo(btc);
        assertThat(persistedBalance.getEthAmount()).isEqualTo(eth);

    }

    @Test
    @Order(2)
    public void testFindTopByUsernameOrderByCreateDateTimeDesc(){

        var balance = new Balance();
        balance.setUsername("Peter");
        balance.setUsdAmount(BigDecimal.ZERO);
        balance.setEthAmount(BigDecimal.ZERO);
        balance.setBtcAmount(BigDecimal.ZERO);

        var balance1 = new Balance();
        balance1.setUsername("Peter");
        balance1.setUsdAmount(BigDecimal.ONE);
        balance1.setEthAmount(BigDecimal.ONE);
        balance1.setBtcAmount(BigDecimal.ONE);

        var balance2 = new Balance();
        balance2.setUsername("John");
        balance2.setUsdAmount(BigDecimal.ZERO);
        balance2.setEthAmount(BigDecimal.ZERO);
        balance2.setBtcAmount(BigDecimal.ZERO);

        var balance3 = new Balance();
        balance3.setUsername("John");
        balance3.setUsdAmount(BigDecimal.TEN);
        balance3.setEthAmount(BigDecimal.TEN);
        balance3.setBtcAmount(BigDecimal.TEN);

        balanceRepository.save(balance);
        balanceRepository.save(balance1);
        balanceRepository.save(balance2);
        balanceRepository.save(balance3);

         var x = balanceRepository.findAll();

        var peterLatestBalance = balanceRepository.findLatestBalanceByUsername("Peter");
        assertThat(peterLatestBalance.get().getBtcAmount()).isEqualTo(BigDecimal.ONE);

        var johnLatestBalance = balanceRepository.findLatestBalanceByUsername("John");
        assertThat(johnLatestBalance.get().getBtcAmount()).isEqualTo(BigDecimal.TEN);

    }

    @Test
    @Order(3)
    public void testFindTopByUsernameOrderByCreateDateTimeDesc_BalanceNotFoundException(){

        try{
            var peterLatestBalance = balanceRepository.findLatestBalanceByUsername("Mary")
                    .orElseThrow(() -> new BalanceNotFoundException("Please create new account for : " ));
            Assertions.fail();
        } catch (BalanceNotFoundException e){
            assertThat(e.getMessage()).contains("Please create new account");
        }


    }


}
