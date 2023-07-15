package com.example.trading.application.repository;

import com.example.trading.application.domain.EthUsdtPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EthUsdtPairRepository extends JpaRepository<EthUsdtPair, Long>, CurrencyPairRepository<EthUsdtPair> {
}
