package com.example.demo.application.transaction;

import com.example.demo.domain.transaction.model.Transaction;
import com.example.demo.domain.transaction.model.TransactionId;
import com.example.demo.domain.transaction.repository.TransactionRepository;
import com.example.demo.domain.user.model.UserId;
import com.example.demo.infra.messaging.transaction.TransactionCreatedEvent;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTransactionUseCase {

  private final TransactionRepository transactionRepository;
  private final TransactionEventPublisher publisher;

  @Timed("transaction.create")
  public TransactionId execute(final CreateTransactionCommand command) {
    final var transactionId = TransactionId.generate();
    final var payerId = UserId.of(command.payer());
    final var transaction = Transaction.builder()
      .id(transactionId)
      .payerId(payerId)
      .payeeId(UserId.of(command.payee()))
      .amountInCents(command.amount() * 100)
      .build();
    transactionRepository.save(transaction);
    publisher.publish(TransactionCreatedEvent.from(transactionId, payerId));
    return transactionId;
  }
}
