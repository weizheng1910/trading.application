package com.example.trading.application.repository;

import com.example.trading.application.domain.BtcUsdtPair;
import com.example.trading.application.domain.EthUsdtPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BtcUsdtPairRepositoryTest {

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

//        var persisted = btcUsdtPairRepository.findAll().get(0);
//        assertThat(persisted.getAsk()).isEqualTo(ask);
//        assertThat(persisted.getBid()).isEqualTo(bid);
//        assertThat(persisted.getCreateDateTime()).isNotNull();

    }

}
