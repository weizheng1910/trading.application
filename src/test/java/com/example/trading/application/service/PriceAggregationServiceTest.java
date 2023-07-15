package com.example.trading.application.service;

import com.example.trading.application.constants.CurrPair;
import com.example.trading.application.dto.BidAsk;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class PriceAggregationServiceTest {

    @Test
    public void testAggregateAndStore(){
        // Given
        var binanceServiceMock = Mockito.mock(BinanceService.class);
        Mockito.when(binanceServiceMock.getBidAskOf(CurrPair.BTCUSDT)).thenReturn(new BidAsk(BigDecimal.ONE,BigDecimal.TEN));

        var huoBiServiceMock = Mockito.mock(HuoBiService.class);
        Mockito.when(huoBiServiceMock.getBidAskOf(CurrPair.BTCUSDT)).thenReturn(new BidAsk(BigDecimal.TEN,BigDecimal.ONE));

        var priceAggregationService = new PriceAggregationService(binanceServiceMock,huoBiServiceMock);

        // When
        var result = priceAggregationService.aggregateAndStore(CurrPair.BTCUSDT);

        // Then
        assertThat(result.getBid()).isEqualTo(BigDecimal.ONE);
        assertThat(result.getAsk()).isEqualTo(BigDecimal.TEN);

    }
}
