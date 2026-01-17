package com.example.demo.application.port.in;

public record CreateTransactionCommand(
  Long payerId,
  Long payeeId,
  Long amountInCents
) {

  public static CreateTransactionCommand of(Long payerId, Long payeeId, Long amountInCents) {
    return new CreateTransactionCommand(payerId, payeeId, amountInCents);
  }
}
