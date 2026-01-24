package com.example.demo.domain.event;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionAuthorizationEvent extends TransactionEvent {


  public TransactionAuthorizationEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }

  public static TransactionAuthorizationEvent of(String key, TransactionId transactionId) {
    return new TransactionAuthorizationEvent(key, transactionId);
  }

  public static TransactionAuthorizationEvent from(TransactionEvent other) {
    return new TransactionAuthorizationEvent(other.getKey(), other.getTransactionId());
  }
}
