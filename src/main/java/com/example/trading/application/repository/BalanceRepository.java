package com.example.trading.application.repository;

import com.example.trading.application.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    @Query(value = "select * from balance where username =:username ORDER BY id desc limit 1", nativeQuery=true)
    Optional<Balance> findLatestBalanceByUsername(String username);
}
