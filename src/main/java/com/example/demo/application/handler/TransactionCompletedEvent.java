package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionCompletedEvent extends TransactionEvent {

  public TransactionCompletedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }

  public static TransactionCompletedEvent of(String key, TransactionId transactionId) {
    return new TransactionCompletedEvent(key, transactionId);
  }

  public static TransactionCompletedEvent from(TransactionEvent other) {
    return new TransactionCompletedEvent(other.getKey(), other.getTransactionId());
  }
}
