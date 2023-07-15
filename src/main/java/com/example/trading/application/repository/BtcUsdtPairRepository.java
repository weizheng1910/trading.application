package com.example.trading.application.repository;

import com.example.trading.application.domain.BtcUsdtPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BtcUsdtPairRepository extends JpaRepository<BtcUsdtPair, Long>, CurrencyPairRepository<BtcUsdtPair> {


}
