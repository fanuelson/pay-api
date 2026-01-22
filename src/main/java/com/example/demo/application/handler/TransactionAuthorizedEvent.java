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
}
