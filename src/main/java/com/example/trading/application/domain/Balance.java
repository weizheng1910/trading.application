package com.example.trading.application.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private BigDecimal usdAmount = BigDecimal.ZERO;

    private BigDecimal ethAmount = BigDecimal.ZERO;

    private BigDecimal btcAmount = BigDecimal.ZERO;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Override
    public String toString(){
        return String.format("Balance - User: %s, USD: %s, ETH: %s, BTC: %s", username, usdAmount,
                ethAmount, btcAmount);
    }
}
