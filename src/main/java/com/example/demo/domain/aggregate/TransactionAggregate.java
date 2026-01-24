package com.example.demo.domain.aggregate;

import com.example.demo.domain.event.*;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.User;
import com.example.demo.domain.model.Wallet;
import com.example.demo.domain.vo.TransactionId;
import lombok.Builder;
import lombok.Data;
import java.util.Deque;
import java.util.LinkedList;
import static java.util.Objects.requireNonNullElse;

@Data
@Builder
public class TransactionAggregate {

  private TransactionId transactionId;
  private Transaction transaction;
  private User payer;
  private User payee;
  private Wallet payerWallet;
  private Wallet payeeWallet;

  private Deque<TransactionEvent> events;

  public Wallet reservePayerBalance() {
    final var wallet = getPayerWallet();
    wallet.debit(transaction.getAmountInCents());
    addEvent(TransactionBalanceReservedEvent.of(payer.getId().value(), transactionId));
    return wallet;
  }

  public Wallet creditPayer() {
    final var wallet = getPayerWallet();
    wallet.credit(transaction.getAmountInCents());
    return wallet;
  }

  public Wallet creditPayee() {
    var wallet = getPayeeWallet();
    wallet.credit(transaction.getAmountInCents());
    addEvent(TransactionCreditedEvent.of(payer.getId().value(), transactionId));
    return wallet;
  }

  public Transaction complete() {
    transaction.completed();
    addEvent(TransactionCompletedEvent.of(payer.getId().value(), transactionId));
    return transaction;
  }

  public Transaction fail(String reason) {
    transaction.failed(reason);
    addEvent(TransactionFailedEvent.of(getPayer().getId().value(), transactionId, reason));
    return transaction;
  }

  public Transaction authorizationFail(String reason) {
    transaction.failed(reason);
    addEvent(TransactionAuthorizationFailedEvent.of(getPayer().getId().value(), transactionId, reason));
    return transaction;
  }

  public Transaction authorize(String authorizationCode) {
    transaction.authorized(authorizationCode);
    addEvent(TransactionAuthorizedEvent.of(payer.getId().value(), transactionId, authorizationCode));
    return transaction;
  }

  public Deque<TransactionEvent> getEvents() {
    return requireNonNullElse(events, new LinkedList<>());
  }

  private void addEvent(TransactionEvent event) {
    getEvents().push(event);
  }

  private TransactionEvent getLastEvent() {
    return getEvents().peek();
  }
}
