package com.example.demo.wallet.domain;

import com.example.demo.shared.domain.UserId;

import java.time.LocalDateTime;


public class Wallet {
  private Long id;
  private UserId userId;
  private long balanceInCents;
  private long version;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
