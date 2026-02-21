package com.example.demo.application.transaction;

import com.example.demo.domain.transaction.model.TransactionId;
import lombok.Getter;

@Getter
public abstract class TransactionEvent {

  private final TransactionId transactionId;

  protected TransactionEvent(TransactionId transactionId) {
    this.transactionId = transactionId;
  }

}
