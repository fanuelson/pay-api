package com.example.demo.domain.event;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionAuthorizationRequestedEvent extends TransactionAuthorizationEvent {

  public TransactionAuthorizationRequestedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }

  public static TransactionAuthorizationRequestedEvent of(String key, TransactionId transactionId) {
    return new TransactionAuthorizationRequestedEvent(key, transactionId);
  }

  public static TransactionAuthorizationRequestedEvent from(TransactionEvent other) {
    return new TransactionAuthorizationRequestedEvent(other.getKey(), other.getTransactionId());
  }

}
