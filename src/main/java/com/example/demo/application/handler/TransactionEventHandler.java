package com.example.demo.application.handler;


import com.example.demo.application.port.out.event.TransactionEventPublisher;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.application.saga.transfer.step.AuthorizeStep;
import com.example.demo.application.saga.transfer.step.NotifyStep;
import com.example.demo.application.saga.transfer.step.ValidateStep;
import com.example.demo.domain.event.*;
import com.example.demo.domain.repository.TransactionAggregateRepository;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventHandler {

  private final WalletRepository walletRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionAggregateRepository transactionAggregateRepository;
  private final TransactionEventPublisher transactionEventPublisher;

  private final ValidateStep validateStep;
  private final AuthorizeStep authorizeStep;
  private final NotifyStep notifyStep;

  public void handle(TransactionRequestedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    validateStep.execute(context);
    TransactionValidatedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionValidatedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var wallet = transactionAggregate.reservePayerBalance();
    walletRepository.update(wallet.getId(), wallet);
    TransactionBalanceReservedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionBalanceReservedEvent event) {
    TransactionAuthorizationRequestedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionAuthorizationRequestedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    authorizeStep.execute(context);
    transactionRepository.save(context.getTransaction());
    final var authorizationCode = context.getTransaction().getAuthorizationCode();
    TransactionAuthorizedEvent.from(event).withAuthorizationCode(authorizationCode).publish(this::publish);
  }


  public void handle(TransactionAuthorizedEvent event) {
    final var transaction = transactionAggregateRepository.findById(event.getTransactionId());
    final var wallet = transaction.creditPayee();
    walletRepository.update(wallet.getId(), wallet);
    TransactionCreditedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionCreditedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var transaction = transactionAggregate.complete();
    transactionRepository.save(transaction);
    TransactionCompletedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionCompletedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    notifyStep.execute(context);
  }

  public void handle(TransactionAuthorizationFailedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var wallet = transactionAggregate.creditPayer();
    walletRepository.update(wallet.getId(), wallet);
    TransactionFailedEvent.of(event.getKey(), event.getTransactionId(), event.getMsg())
      .publish(transactionEventPublisher::publish);
  }

  public void handle(TransactionFailedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var transaction = transactionAggregate.fail(event.getCause());
    transactionRepository.save(transaction);
  }

  private void publish(TransactionEvent event) {
    transactionEventPublisher.publish(event);
  }
}
