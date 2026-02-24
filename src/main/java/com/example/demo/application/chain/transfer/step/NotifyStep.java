package com.example.demo.application.chain.transfer.step;

import com.example.demo.application.chain.transfer.TransferContext;
import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.usecase.CreateNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifyStep implements TransferHandler {

  private final CreateNotificationUseCase createNotificationUseCase;

  @Override
  public String name() {
    return "Notify";
  }

  @Override
  public void execute(TransferContext context) {
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

  private String formatAmount(Long cents) {
    return String.format("%.2f", cents / 100.0);
  }
}
