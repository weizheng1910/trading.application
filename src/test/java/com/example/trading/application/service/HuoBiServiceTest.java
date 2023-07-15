package com.example.trading.application.service;

import com.example.trading.application.constants.CurrPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {HuoBiService.class, BinanceService.class})
public class HuoBiServiceTest {
  @Autowired HuoBiService huoBiService;

  @Autowired BinanceService binanceService;

  @Test
  public void testHuoBiService_EthUsdT() {
    var bidAsk = huoBiService.getBidAskOf(CurrPair.ETHUSDT);
    assertThat(bidAsk.getBid()).isNotNull();
    assertThat(bidAsk.getAsk()).isNotNull();
  }

  @Test
  public void testHuoBiService_BtcUsdT() {
    var bidAsk = huoBiService.getBidAskOf(CurrPair.BTCUSDT);
    assertThat(bidAsk.getBid()).isNotNull();
    assertThat(bidAsk.getAsk()).isNotNull();
  }

  @Test
  public void testBinanceService_EthUsdT() {
    var bidAsk = binanceService.getBidAskOf(CurrPair.ETHUSDT);
    assertThat(bidAsk.getBid()).isNotNull();
    assertThat(bidAsk.getAsk()).isNotNull();
  }

  @Test
  public void testBinanceService_BtcUsdT() {
    var bidAsk = binanceService.getBidAskOf(CurrPair.BTCUSDT);
    assertThat(bidAsk.getBid()).isNotNull();
    assertThat(bidAsk.getAsk()).isNotNull();
  }
}
