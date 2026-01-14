package com.example.demo.application.port.out.event;

import java.time.LocalDateTime;

public record TransferEvent(
  String transactionId,
  long payerId,
  long payeeId,
  long amountInCents,
  String status,
  String authorizationCode,
  LocalDateTime timestamp
) {

  public TransferEvent(
    String transactionId,
    long payerId,
    long payeeId,
    long amountInCents,
    String status,
    String authorizationCode
  ) {
    this(
      transactionId,
      payerId,
      payeeId,
      amountInCents,
      status,
      authorizationCode,
      LocalDateTime.now()
    );
  }
}