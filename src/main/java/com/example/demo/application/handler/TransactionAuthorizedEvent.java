package com.example.demo.application.handler;

import lombok.Getter;

@Getter
public class TransactionAuthorizedEvent extends TransactionEvent {
  private final String authorizationCode;

  public TransactionAuthorizedEvent(TransactionEvent other, String authorizationCode) {
    super(other);
    this.authorizationCode = authorizationCode;
  }

  public static TransactionAuthorizedEvent from(TransactionEvent other) {
    return new TransactionAuthorizedEvent(other, null);
  }

  public TransactionAuthorizedEvent withAuthorizationCode(String authorizationCode) {
    return new TransactionAuthorizedEvent(this, authorizationCode);
  }
}
