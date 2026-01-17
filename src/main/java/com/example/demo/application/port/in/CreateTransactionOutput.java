package com.example.demo.application.port.in;

public record CreateTransactionOutput(String transactionId) {

  public static CreateTransactionOutput of(String transactionId) {
    return new CreateTransactionOutput(transactionId);
  }
}
