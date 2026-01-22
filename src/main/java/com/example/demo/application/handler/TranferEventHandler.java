package com.example.demo.application.handler;


import com.example.demo.application.port.out.event.TransactionEventPublisher;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.application.saga.transfer.step.*;
import com.example.demo.domain.repository.TransactionAggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TranferEventHandler {

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
    transactionEventPublisher.publish(new TransactionValidatedEvent(event.getKey(), event.getTransactionId()));
  }

  public void handle(TransactionValidatedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    reserveBalanceStep.execute(context);
    transactionEventPublisher.publish(new TransactionBalanceReservedEvent(event.getKey(), event.getTransactionId()));
    //TODO credit payee
  }

  public void handle(TransactionBalanceReservedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    authorizeStep.execute(context);
    final var authorizationCode = context.getTransaction().getAuthorizationCode();
    transactionEventPublisher.publish(new TransactionAuthorizedEvent(event.getKey(), event.getTransactionId(), authorizationCode));
  }

  public void handle(TransactionAuthorizedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    creditStep.execute(context);
    transactionEventPublisher.publish(new TransactionCreditedEvent(event.getKey(), event.getTransactionId()));
  }

  public void handle(TransactionCreditedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    completeStep.execute(context);
    transactionEventPublisher.publish(new TransactionCompletedEvent(event.getKey(), event.getTransactionId()));
  }

  public void handle(TransactionCompletedEvent event) {
    final var transactionAggregate = transactionAggregateRepository.findById(event.getTransactionId());
    final var context = TransferSagaContext.of(transactionAggregate);
    notifyStep.execute(context);
  }
}
