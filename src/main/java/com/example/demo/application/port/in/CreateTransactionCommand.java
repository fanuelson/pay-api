package com.example.demo.application.port.in;

import com.example.demo.domain.vo.UserId;
public record CreateTransactionCommand(
  UserId payerId,
  UserId payeeId,
  Long amountInCents
) {

  public static CreateTransactionCommand of(UserId payerId, UserId payeeId, Long amountInCents) {
    return new CreateTransactionCommand(payerId, payeeId, amountInCents);
  }
}
