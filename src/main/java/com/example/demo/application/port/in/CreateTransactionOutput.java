package com.example.demo.application.port.in;

import com.example.demo.domain.vo.TransactionId;

public record CreateTransactionOutput(TransactionId transactionId) {

  public static CreateTransactionOutput of(TransactionId transactionId) {
    return new CreateTransactionOutput(transactionId);
  }
}
