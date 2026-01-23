package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionCreditedEvent extends TransactionEvent {

  public TransactionCreditedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }

  public static TransactionCreditedEvent of(String key, TransactionId transactionId) {
    return new TransactionCreditedEvent(key, transactionId);
  }

  public static TransactionCreditedEvent from(TransactionEvent other) {
    return of(other.getKey(), other.getTransactionId());
  }

}
