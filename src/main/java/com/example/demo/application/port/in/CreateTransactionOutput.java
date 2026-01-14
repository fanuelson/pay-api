package com.example.demo.application.port.in;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateTransactionOutput {
  private final String transactionId;

  public static CreateTransactionOutput of(String transactionId) {
    return new CreateTransactionOutput(transactionId);
  }
}
