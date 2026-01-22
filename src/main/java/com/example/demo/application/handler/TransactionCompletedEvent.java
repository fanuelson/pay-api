package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionCompletedEvent extends TransactionEvent {

  public TransactionCompletedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }
}
