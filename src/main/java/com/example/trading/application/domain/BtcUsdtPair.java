package com.example.trading.application.domain;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BTCUSDT")
public class BtcUsdtPair extends CurrencyPair {
}

