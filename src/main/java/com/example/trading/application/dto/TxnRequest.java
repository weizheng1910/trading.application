package com.example.trading.application.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class TxnRequest {

  @NotEmpty(message = "Username must not be empty")
  private String username;

  @Pattern(regexp = "BtcUsdt|EthUsdt", message = "Valid values are \"BtcUsdt\" or \"EtcUsdt\"")
  @NotEmpty(message = "pair must not be empty. Valid values are \"BtcUsdt\" or \"EtcUsdt\"")
  private String pair;

  @NotEmpty(message = "Type of order must not be empty. Valid values are \"BUY\" or \"SELL\"")
  private String order;

  @Digits(integer=4, fraction=2)
  private BigDecimal amount;
}
