package com.example.demo.application.handler;

import lombok.Getter;

@Getter
public class TransactionCompletedEvent extends TransactionEvent {

  public TransactionCompletedEvent(TransactionEvent other) {
    super(other);
  }

  public static TransactionCompletedEvent from(TransactionEvent other) {
    return new TransactionCompletedEvent(other);
  }
}
