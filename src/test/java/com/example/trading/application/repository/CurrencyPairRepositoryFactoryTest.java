package com.example.trading.application.repository;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.repository.CurrencyPairRepositoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.util.Predicates.isTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CurrencyPairRepositoryFactoryTest {

    @Autowired
    CurrencyPairRepositoryFactory currencyPairRepositoryFactory;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void test(){
        var x = currencyPairRepositoryFactory.getRepository(CurrPair.ETHUSDT);
        assertThat(x instanceof EthUsdtPairRepository).isTrue();
    }

}
