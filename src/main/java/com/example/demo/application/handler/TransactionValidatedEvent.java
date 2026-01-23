package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;


@Getter
public class TransactionValidatedEvent extends TransactionEvent {

  public TransactionValidatedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }

  public static TransactionValidatedEvent from(TransactionEvent other) {
    return new TransactionValidatedEvent(other.getKey(), other.getTransactionId());
  }
}
