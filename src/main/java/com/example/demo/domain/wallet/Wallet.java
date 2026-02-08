package com.example.demo.domain.wallet;

import java.time.LocalDateTime;


public class Wallet {

  private WalletId id;
  private Long userId;
  private Long balanceInCents;
  private int version;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
