package com.example.demo.application.port.in;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateTransactionCommand {

  private final Long payerId;
  private final Long payeeId;
  private final Long amountInCents;

  public static CreateTransactionCommand of(Long payerId, Long payeeId, Long amountInCents) {
    return new CreateTransactionCommand(payerId, payeeId, amountInCents);
  }
}
