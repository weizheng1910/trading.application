package com.example.trading.application.repository;


import com.example.trading.application.domain.CurrencyPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyPairRepository<T extends CurrencyPair> extends JpaRepository<T, Long> {
   T findTopByOrderByIdDesc(); // Fetches latest currency pair

}
