package com.example.demo.application.handler;

import lombok.Getter;

@Getter
public class TransactionAuthorizationFailedEvent extends TransactionEvent {

  private final String msg;

  public TransactionAuthorizationFailedEvent(TransactionEvent other, String msg) {
    super(other);
    this.msg = msg;
  }

  public static TransactionAuthorizationFailedEvent from(TransactionEvent other) {
    return new TransactionAuthorizationFailedEvent(other, null);
  }

  public TransactionAuthorizationFailedEvent withMsg(String msg) {
    return new TransactionAuthorizationFailedEvent(this, msg);
  }
}
