package com.example.demo.domain.event;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionRequestedEvent extends TransactionEvent {

  public TransactionRequestedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }

  public static TransactionRequestedEvent of(String key, TransactionId transactionId) {
    return new TransactionRequestedEvent(key, transactionId);
  }
}
