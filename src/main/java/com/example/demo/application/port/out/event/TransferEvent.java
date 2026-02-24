package com.example.demo.application.port.out.event;

import java.time.LocalDateTime;

public record TransferEvent(
  String transactionId,
  long payerId,
  long payeeId,
  long amountInCents,
  String status,
  String authorizationCode,
  LocalDateTime timestamp,
  String statusDetails
) {

  public TransferEvent(
    String transactionId,
    long payerId,
    long payeeId,
    long amountInCents,
    String status,
    String authorizationCode,
    String statusDetails
  ) {
    this(
      transactionId,
      payerId,
      payeeId,
      amountInCents,
      status,
      authorizationCode,
      LocalDateTime.now(),
      statusDetails
    );
  }

  public TransferEvent withStatus(String newStatus) {
    return new TransferEvent(
      transactionId,
      payerId,
      payeeId,
      amountInCents,
      newStatus,
      authorizationCode,
      statusDetails
    );
  }

  public TransferEvent withStatusDetails(String newStatusDetails) {
    return new TransferEvent(
      transactionId,
      payerId,
      payeeId,
      amountInCents,
      status,
      authorizationCode,
      newStatusDetails
    );
  }

  public TransferEvent withAuthorizationCode(String authorizationCode) {
    return new TransferEvent(
      transactionId,
      payerId,
      payeeId,
      amountInCents,
      status,
      authorizationCode,
      statusDetails
    );
  }
}