package com.example.demo.wallet.domain;

import com.example.demo.shared.domain.UserId;
import com.example.demo.wallet.domain.exception.InsufficientBalanceException;
import lombok.Getter;

@Getter
public final class Wallet {

  private final UserId userId;
  private final long balanceInCents;

  public Wallet(UserId userId, long balanceInCents) {
    if (balanceInCents < 0) {
      throw new IllegalStateException("Saldo da carteira não pode ser negativo");
    }
    this.userId = userId;
    this.balanceInCents = balanceInCents;
  }

  public Wallet debit(long amountInCents) {
    requirePositive(amountInCents);
    if (amountInCents > balanceInCents) {
      throw new InsufficientBalanceException(userId, amountInCents, balanceInCents);
    }
    return new Wallet(userId, balanceInCents - amountInCents);
  }

  public Wallet credit(long amountInCents) {
    requirePositive(amountInCents);
    return new Wallet(userId, balanceInCents + amountInCents);
  }

  private static void requirePositive(long amountInCents) {
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Valor da operação deve ser positivo");
    }
  }
}
