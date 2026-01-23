package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionAuthorizedEvent extends TransactionEvent {
  private final String authorizationCode;

  public TransactionAuthorizedEvent(String key, TransactionId transactionId, String authorizationCode) {
    super(key, transactionId);
    this.authorizationCode = authorizationCode;
  }

  public static TransactionAuthorizedEvent of(String key, TransactionId transactionId, String authorizationCode) {
    return new TransactionAuthorizedEvent(key, transactionId, authorizationCode);
  }

  public static TransactionAuthorizedEvent from(TransactionEvent other) {
    return new TransactionAuthorizedEvent(other.getKey(), other.getTransactionId(), null);
  }

  public TransactionAuthorizedEvent withAuthorizationCode(String authorizationCode) {
    return new TransactionAuthorizedEvent(this.getKey(), this.getTransactionId(), authorizationCode);
  }
}
