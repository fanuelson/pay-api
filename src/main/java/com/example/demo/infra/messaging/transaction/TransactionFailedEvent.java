package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.transaction.TransactionEvent;
import com.example.demo.domain.transaction.model.TransactionId;
import lombok.Getter;

@Getter
public class TransactionFailedEvent extends TransactionEvent {

  private final String cause;

  public TransactionFailedEvent(TransactionId transactionId, String cause) {
    super(transactionId);
    this.cause = cause;
  }

  public static TransactionFailedEvent from(TransactionId transactionId, String cause) {
    return new TransactionFailedEvent(transactionId, cause);
  }
}
