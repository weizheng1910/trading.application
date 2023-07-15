package com.example.trading.application.service;

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
    var bidAsk = huoBiService.getEthUsdT();
    assertThat(bidAsk.getBid()).isNotNull();
    assertThat(bidAsk.getAsk()).isNotNull();
  }

  @Test
  public void testHuoBiService_BtcUsdT() {
    var bidAsk = huoBiService.getBtcUsdT();
    assertThat(bidAsk.getBid()).isNotNull();
    assertThat(bidAsk.getAsk()).isNotNull();
  }

  @Test
  public void testBinanceService_EthUsdT() {
    var bidAsk = binanceService.getEthUsdT();
    assertThat(bidAsk.getBid()).isNotNull();
    assertThat(bidAsk.getAsk()).isNotNull();
  }

  @Test
  public void testBinanceService_BtcUsdT() {
    var bidAsk = binanceService.getBtcUsdT();
    assertThat(bidAsk.getBid()).isNotNull();
    assertThat(bidAsk.getAsk()).isNotNull();
  }
}
