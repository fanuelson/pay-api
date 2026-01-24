package com.example.demo.domain.event;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionBalanceReservedEvent extends TransactionEvent {

  public TransactionBalanceReservedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }

  public static TransactionBalanceReservedEvent of(String key, TransactionId transactionId) {
    return new TransactionBalanceReservedEvent(key, transactionId);
  }

  public static TransactionBalanceReservedEvent from(TransactionEvent other) {
    return new TransactionBalanceReservedEvent(other.getKey(), other.getTransactionId());
  }

}
