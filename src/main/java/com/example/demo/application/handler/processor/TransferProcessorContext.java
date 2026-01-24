package com.example.demo.application.handler.processor;

import com.example.demo.domain.aggregate.TransactionAggregate;
import com.example.demo.domain.event.TransferEvent;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.User;
import com.example.demo.domain.model.Wallet;
import com.example.demo.domain.vo.TransactionId;

public record TransferProcessorContext(
  TransferEvent event,
  TransactionAggregate aggregate
) {

  public static TransferProcessorContext of(TransferEvent event, TransactionAggregate aggregate) {
    return new TransferProcessorContext(event, aggregate);
  }

  public TransactionId getTransactionId() {
    return aggregate.getTransactionId();
  }

  public Transaction getTransaction() {
    return aggregate.getTransaction();
  }

  public User getPayer() {
    return aggregate.getPayer();
  }

  public User getPayee() {
    return aggregate.getPayee();
  }

  public Wallet getPayerWallet() {
    return aggregate.getPayerWallet();
  }

  public Wallet getPayeeWallet() {
    return aggregate.getPayeeWallet();
  }

  public Long getPayerId() {
    return aggregate.getPayer().getId().asLong();
  }

  public Long getPayeeId() {
    return aggregate.getPayee().getId().asLong();
  }

  public Long getAmountInCents() {
    return aggregate.getTransaction().getAmountInCents();
  }

  public String getCause() {
    return event.getCause();
  }
}
