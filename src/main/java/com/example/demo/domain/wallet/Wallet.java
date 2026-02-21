package com.example.demo.domain.wallet;

import com.example.demo.domain.exception.BusinessException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

@Getter
@With(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Wallet {

  private final WalletId id;
  private final Long userId;
  private final Long balanceInCents;

  public Wallet debit(Long amountInCents) {
    if (balanceInCents < amountInCents) {
      throw new BusinessException("Insufficient balance");
    }

    return withBalanceInCents(balanceInCents - amountInCents);
  }

  public Wallet credit(Long amountInCents) {
    return withBalanceInCents(balanceInCents + amountInCents);
  }
}
