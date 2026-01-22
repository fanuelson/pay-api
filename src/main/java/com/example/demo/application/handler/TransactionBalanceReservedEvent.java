package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;

public class TransactionBalanceReservedEvent extends TransactionEvent {
  public TransactionBalanceReservedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }
}
