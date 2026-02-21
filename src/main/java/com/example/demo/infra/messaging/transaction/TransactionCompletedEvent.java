package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.transaction.TransactionEvent;
import com.example.demo.domain.transaction.model.TransactionId;

public class TransactionCompletedEvent extends TransactionEvent {

  public TransactionCompletedEvent(TransactionId transactionId) {
    super(transactionId);
  }

  public static TransactionCompletedEvent from(TransactionId transactionId) {
    return new TransactionCompletedEvent(transactionId);
  }
}
