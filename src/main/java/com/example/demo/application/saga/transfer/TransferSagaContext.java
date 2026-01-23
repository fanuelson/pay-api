package com.example.demo.application.saga.transfer;

import com.example.demo.domain.aggregate.TransactionAggregate;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.User;
import com.example.demo.domain.model.Wallet;
import com.example.demo.domain.vo.TransactionId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferSagaContext {

  private TransactionAggregate transactionAggregate;

  public static TransferSagaContext of(TransactionAggregate transactionAggregate) {
    return new TransferSagaContext(transactionAggregate);
  }

  public Transaction getTransaction() {
    return transactionAggregate.getTransaction();
  }

  public TransactionId getTransactionId() {
    return getTransactionAggregate().getTransactionId();
  }

  public Long getPayerId() {
    return transactionAggregate.getPayer().getId().asLong();
  }

  public Long getPayeeId() {
    return transactionAggregate.getPayee().getId().asLong();
  }

  public Long getAmountInCents() {
    return transactionAggregate.getTransaction().getAmountInCents();
  }

  public void setTransaction(Transaction transaction) {
    transactionAggregate.setTransaction(transaction);
  }

  public Wallet getPayerWallet() {
    return transactionAggregate.getPayerWallet();
  }

  public Wallet getPayeeWallet() {
    return transactionAggregate.getPayeeWallet();
  }

  public void setPayerWallet(Wallet payerWallet) {
    transactionAggregate.setPayerWallet(payerWallet);
  }

  public void setPayeeWallet(Wallet payeeWallet) {
    transactionAggregate.setPayeeWallet(payeeWallet);
  }

  public User getPayer() {
    return transactionAggregate.getPayer();
  }

  public User getPayee() {
    return transactionAggregate.getPayee();
  }
}
