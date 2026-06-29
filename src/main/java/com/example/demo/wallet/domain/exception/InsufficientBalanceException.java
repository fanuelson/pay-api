package com.example.demo.wallet.domain.exception;

import com.example.demo.shared.domain.UserId;

public class InsufficientBalanceException extends RuntimeException {

  public InsufficientBalanceException(UserId userId, long requested, long available) {
    super("Saldo insuficiente para o usuário [%s]: solicitado [%d], disponível [%d]"
        .formatted(userId.value(), requested, available));
  }
}
