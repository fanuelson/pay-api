package com.example.demo.application.saga.transfer.step;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.saga.SagaStep;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.application.usecase.CreateNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyStep implements SagaStep<TransferSagaContext> {

  private final CreateNotificationUseCase createNotificationUseCase;

  @Override
  public String getName() {
    return "Notify";
  }

  @Override
  public void execute(TransferSagaContext context) {
    var transaction = context.getTransaction();
    try {
      createNotificationUseCase.execute(CreateNotificationCommand.of(
        transaction.getId(),
        context.getPayerId(),
        "Transferência de R$ " + formatAmount(context.getAmountInCents()) + " realizada com sucesso"
      ));

      createNotificationUseCase.execute(CreateNotificationCommand.of(
        transaction.getId(),
        context.getPayeeId(),
        "Você recebeu R$ " + formatAmount(context.getAmountInCents())
      ));
    } catch (Exception e) {
      log.warn("Not throwing to not call compensate");
      log.error("Error creating notification", e);
    }

  }

  @Override
  public void compensate(TransferSagaContext context, String cause) {
  }

  private String formatAmount(Long cents) {
    return String.format("%.2f", cents / 100.0);
  }
}
