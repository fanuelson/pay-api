package com.example.demo.application.handler.processor;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.usecase.CreateNotificationUseCase;
import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedEventProcessor implements TransferEventProcessor {

  private final CreateNotificationUseCase createNotificationUseCase;

  @Override
  public TransactionEventType getEventType() {
    return TransactionEventType.COMPLETED;
  }

  @Override
  public Optional<TransferEvent> process(TransferProcessorContext context) {
    try {
      createNotificationUseCase.execute(CreateNotificationCommand.of(
        context.getTransaction().getId(),
        context.getPayerId(),
        "Transferência de R$ " + formatAmount(context.getAmountInCents()) + " realizada com sucesso"
      ));

      createNotificationUseCase.execute(CreateNotificationCommand.of(
        context.getTransaction().getId(),
        context.getPayeeId(),
        "Você recebeu R$ " + formatAmount(context.getAmountInCents())
      ));
    } catch (Exception e) {
      log.error("Error sending notification for transactionId={}", context.getTransactionId(), e);
    }

    return Optional.empty();
  }

  private String formatAmount(Long cents) {
    return String.format("%.2f", cents / 100.0);
  }
}
