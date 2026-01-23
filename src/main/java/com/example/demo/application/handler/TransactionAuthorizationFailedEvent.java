package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionAuthorizationFailedEvent extends TransactionEvent {

  private final String msg;

  public TransactionAuthorizationFailedEvent(String key, TransactionId transactionId, String msg) {
    super(key, transactionId);
    this.msg = msg;
  }

  public static TransactionAuthorizationFailedEvent from(TransactionEvent other) {
    return new TransactionAuthorizationFailedEvent(other.getKey(), other.getTransactionId(), null);
  }

  public TransactionAuthorizationFailedEvent withMsg(String msg) {
    return new TransactionAuthorizationFailedEvent(this.getKey(), this.getTransactionId(), msg);
  }
}
