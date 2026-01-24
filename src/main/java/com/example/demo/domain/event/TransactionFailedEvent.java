package com.example.demo.domain.event;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionFailedEvent extends TransactionEvent {

  private final String cause;

  public TransactionFailedEvent(String key, TransactionId transactionId, String cause) {
    super(key, transactionId);
    this.cause = cause;
  }

  public static TransactionFailedEvent of(String key, TransactionId transactionId, String cause) {
    return new TransactionFailedEvent(key, transactionId, cause);
  }

  public static TransactionFailedEvent from(TransactionEvent other, String cause) {
    return new TransactionFailedEvent(other.getKey(), other.getTransactionId(), cause);
  }
}
