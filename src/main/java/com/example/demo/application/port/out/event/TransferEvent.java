package com.example.demo.application.port.out.event;

import com.example.demo.domain.helper.DateTimeHelper;
import com.example.demo.domain.vo.TransactionId;
import java.time.LocalDateTime;

public record TransferEvent(
  TransactionId transactionId,
  long payerId,
  long payeeId,
  long amountInCents,
  String status,
  String authorizationCode,
  TransferEventType type,
  LocalDateTime timestamp
) {

  public TransferEvent(
    TransactionId transactionId,
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
      null,
      DateTimeHelper.now()
    );
  }

  public TransferEvent(
    TransactionId transactionId,
    long payerId,
    long payeeId,
    long amountInCents,
    String status,
    TransferEventType type,
    String authorizationCode
  ) {
    this(
      transactionId,
      payerId,
      payeeId,
      amountInCents,
      status,
      authorizationCode,
      type,
      DateTimeHelper.now()
    );
  }

  public TransferEvent(
    TransactionId transactionId,
    TransferEventType type
  ) {
    this(
      transactionId,
      0,
      0,
      0,
      null,
      null,
      type,
      DateTimeHelper.now()
    );
  }

  public TransferEvent withAuthorizationCode(String authorizationCode) {
    return new TransferEvent(transactionId, payerId, payeeId, amountInCents, status, type, authorizationCode);
  }

}