package com.example.demo.application.saga.transfer.step;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.saga.SagaStep;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.application.usecase.CreateNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
  }

  @Override
  public void compensate(TransferSagaContext context, String cause) {
  }

  private String formatAmount(Long cents) {
    return String.format("%.2f", cents / 100.0);
  }
}
