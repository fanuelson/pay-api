package com.example.demo.domain.aggregate;

import com.example.demo.domain.event.TransactionEventKey;
import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
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

  private Deque<TransferEvent> events;

  public Wallet reservePayerBalance() {
    final var wallet = getPayerWallet();
    wallet.debit(transaction.getAmountInCents());
    addEvent(TransactionEventType.BALANCE_RESERVED);
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
    addEvent(TransactionEventType.CREDITED);
    return wallet;
  }

  public Transaction complete() {
    transaction.completed();
    addEvent(TransactionEventType.COMPLETED);
    return transaction;
  }

  public Transaction fail(String reason) {
    transaction.failed(reason);
    addEvent(TransferEvent.of(getKey(), TransactionEventType.FAILED, transactionId, null, reason));
    return transaction;
  }

  public Transaction authorizationFail(String reason) {
    transaction.failed(reason);
    addEvent(TransferEvent.of(getKey(), TransactionEventType.AUTHORIZATION_FAILED, transactionId, null, reason));
    return transaction;
  }

  public Transaction authorize(String authorizationCode) {
    transaction.authorized(authorizationCode);
    addEvent(TransferEvent.of(getKey(), TransactionEventType.AUTHORIZED, transactionId, authorizationCode, null));
    return transaction;
  }

  public Deque<TransferEvent> getEvents() {
    return requireNonNullElse(events, new LinkedList<>());
  }

  private TransactionEventKey getKey() {
    return TransactionEventKey.of(payer.getId().value());
  }

  private void addEvent(TransactionEventType type) {
    addEvent(TransferEvent.of(getKey(), type, transactionId));
  }

  private void addEvent(TransferEvent event) {
    getEvents().push(event);
  }

  private TransferEvent getLastEvent() {
    return getEvents().peek();
  }
}
