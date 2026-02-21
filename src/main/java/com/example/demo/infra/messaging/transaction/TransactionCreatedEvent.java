package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.transaction.TransactionEvent;
import com.example.demo.domain.transaction.model.TransactionId;
import com.example.demo.domain.user.model.UserId;
import lombok.Getter;

@Getter
public class TransactionCreatedEvent extends TransactionEvent {

  private final UserId payerId;

  public TransactionCreatedEvent(TransactionId transactionId, UserId payerId) {
    super(transactionId);
    this.payerId = payerId;
  }

  public static TransactionCreatedEvent from(TransactionId transactionId, UserId payerId) {
    return new TransactionCreatedEvent(transactionId, payerId);
  }
}
