package com.example.demo.application.transaction;

import com.example.demo.application.notification.event.CreateNotificationEvent;
import com.example.demo.application.notification.CreateNotificationUseCase;
import com.example.demo.domain.transaction.repository.TransactionRepository;
import com.example.demo.infra.messaging.transaction.TransactionCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionCompletedNotification {

  private final TransactionRepository transactionRepository;
  private final CreateNotificationUseCase notificationEngine;

  public void execute(final TransactionCompletedEvent event) {
    final var transaction = transactionRepository.findById(event.getTransactionId())
      .orElseThrow();

    final var payerMessage = String.format(
      "Transfer of %d cents completed successfully. Transaction: %s",
      transaction.getAmountInCents(),
      transaction.getId().value()
    );

    notificationEngine.handle(
      CreateNotificationEvent.of(transaction.getPayerId(), payerMessage)
    );

    final var payeeMessage = String.format(
      "You received a transfer of %d cents. Transaction: %s",
      transaction.getAmountInCents(),
      transaction.getId().value()
    );

    notificationEngine.handle(
      CreateNotificationEvent.of(transaction.getPayeeId(), payeeMessage)
    );
  }
}
