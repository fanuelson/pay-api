package com.example.demo.domain.event;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionAuthorizationFailedEvent extends TransactionAuthorizationEvent {

  private final String msg;

  public TransactionAuthorizationFailedEvent(String key, TransactionId transactionId, String msg) {
    super(key, transactionId);
    this.msg = msg;
  }

  public static TransactionAuthorizationFailedEvent of(String key, TransactionId transactionId, String msg) {
    return new TransactionAuthorizationFailedEvent(key, transactionId, msg);
  }

  public static TransactionAuthorizationFailedEvent from(TransactionEvent other) {
    return of(other.getKey(), other.getTransactionId(), null);
  }

  public TransactionAuthorizationFailedEvent withMsg(String msg) {
    return of(this.getKey(), this.getTransactionId(), msg);
  }
}
