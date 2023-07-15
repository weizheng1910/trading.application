package com.example.trading.application.service;

import com.example.trading.application.repository.BtcUsdtPairRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PriceAggregationJobTest {

    @Autowired
    BtcUsdtPairRepository btcUsdtPairRepository;

    @Test
    public void testSchedulerIsWorking() throws InterruptedException {

        Thread.sleep(3000);

        var numberOfPairsSaved = btcUsdtPairRepository.findAll().size();

        assertThat(numberOfPairsSaved).isNotZero();
    }

}
