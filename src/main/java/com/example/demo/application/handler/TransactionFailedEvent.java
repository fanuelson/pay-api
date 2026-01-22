package com.example.demo.application.handler;

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
}
