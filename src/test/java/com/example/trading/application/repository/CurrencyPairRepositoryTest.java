package com.example.trading.application.repository;

import com.example.trading.application.domain.BtcUsdtPair;
import com.example.trading.application.domain.EthUsdtPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CurrencyPairRepositoryTest {

    @Autowired
    private BtcUsdtPairRepository btcUsdtPairRepository;

    @Autowired
    private EthUsdtPairRepository ethUsdtPairRepository;

    @Test
    public void testOnSave(){

        BigDecimal ask = BigDecimal.ONE;
        BigDecimal bid = BigDecimal.TEN;

        var pair = new BtcUsdtPair();
        pair.setAsk(ask);
        pair.setBid(bid);

        var pair1 = new EthUsdtPair();
        pair1.setAsk(ask);
        pair1.setBid(bid);

        btcUsdtPairRepository.save(pair);
        ethUsdtPairRepository.save(pair1);

        assertThat(btcUsdtPairRepository.findAll().size()).isEqualTo(1);
        assertThat(ethUsdtPairRepository.findAll().size()).isEqualTo(1);

    }

    @Test
    public void testRetrieveLatest(){

        var pair = new BtcUsdtPair();
        pair.setAsk(BigDecimal.ONE);
        pair.setBid(BigDecimal.ONE);

        var pair1 = new BtcUsdtPair();
        pair1.setAsk(BigDecimal.TEN);
        pair1.setBid(BigDecimal.TEN);

        var pair2 = new BtcUsdtPair();
        pair2.setAsk(BigDecimal.ZERO);
        pair2.setBid(BigDecimal.ZERO);

        var pairA = new EthUsdtPair();
        pairA.setAsk(BigDecimal.ONE);
        pairA.setBid(BigDecimal.ONE);

        var pairB = new EthUsdtPair();
        pairB.setAsk(BigDecimal.TEN);
        pairB.setBid(BigDecimal.TEN);

        var pairC = new EthUsdtPair();
        pairC.setAsk(new BigDecimal("999"));
        pairC.setBid(new BigDecimal("999"));


        btcUsdtPairRepository.save(pair);
        btcUsdtPairRepository.save(pair1);
        btcUsdtPairRepository.save(pair2);

        ethUsdtPairRepository.save(pairA);
        ethUsdtPairRepository.save(pairB);
        ethUsdtPairRepository.save(pairC);

        var result = btcUsdtPairRepository.findTopByOrderByIdDesc();

        assertThat(result.getAsk()).isEqualTo(BigDecimal.ZERO);

    }

}
