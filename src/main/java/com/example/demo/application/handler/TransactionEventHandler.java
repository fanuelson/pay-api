package com.example.demo.application.handler;


import com.example.demo.application.port.out.event.TransactionEventPublisher;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.application.saga.transfer.step.*;
import com.example.demo.domain.repository.TransactionAggregateRepository;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventHandler {

  private final TransactionRepository transactionRepository;
  private final TransactionAggregateRepository transactionAggregateRepository;
  private final TransactionEventPublisher transactionEventPublisher;

  private final ValidateStep validateStep;
  private final ReserveBalanceStep reserveBalanceStep;
  private final AuthorizeStep authorizeStep;
  private final CreditStep creditStep;
  private final CompleteStep completeStep;
  private final NotifyStep notifyStep;

  public void handle(TransactionRequestedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    validateStep.execute(context);
    TransactionValidatedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionValidatedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    reserveBalanceStep.execute(context);
    TransactionBalanceReservedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionBalanceReservedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    authorizeStep.execute(context);
    final var authorizationCode = context.getTransaction().getAuthorizationCode();
    publish(TransactionAuthorizedEvent.from(event).withAuthorizationCode(authorizationCode));
  }

  public void handle(TransactionAuthorizedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    creditStep.execute(context);
    TransactionCreditedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionCreditedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    completeStep.execute(context);
    TransactionCompletedEvent.from(event).publish(this::publish);
  }

  public void handle(TransactionCompletedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    notifyStep.execute(context);
  }

  public void handle(TransactionAuthorizationFailedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    reserveBalanceStep.compensate(context, event.getMsg());
    TransactionFailedEvent.of(event.getKey(), event.getTransactionId(), event.getMsg())
      .publish(transactionEventPublisher::publish);
  }

  public void handle(TransactionFailedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    final var transaction = context.getTransaction();
    transaction.failed(event.getCause());
    transactionRepository.save(transaction);
  }

  private void publish(TransactionEvent event) {
    transactionEventPublisher.publish(event);
  }
}
