package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;

@Getter
public class TransactionCreditedEvent extends TransactionEvent {

  public TransactionCreditedEvent(String key, TransactionId transactionId) {
    super(key, transactionId);
  }
}
