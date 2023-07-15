package com.example.trading.application.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("ETHUSDT")
public class EthUsdtPair extends CurrencyPair {
}


