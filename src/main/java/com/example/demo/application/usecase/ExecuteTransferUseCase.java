package com.example.demo.application.usecase;

import com.example.demo.application.port.in.ExecuteTransferCommand;
import com.example.demo.application.saga.SagaOrchestrator;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.application.saga.transfer.step.*;
import com.example.demo.domain.repository.TransactionAggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecuteTransferUseCase {

  private final TransactionAggregateRepository transactionAggregateRepository;
  private final ValidateStep validateStep;
  private final ReserveBalanceStep reserveBalanceStep;
  private final AuthorizeStep authorizeStep;
  private final CreditStep creditStep;
  private final CompleteStep completeStep;
  private final NotifyStep notifyStep;

  public void execute(ExecuteTransferCommand command) {
    final var transactionAggregate = transactionAggregateRepository.findById(command.transactionId());
    var context =  TransferSagaContext.of(transactionAggregate);

    var saga = SagaOrchestrator.of(
      validateStep,
      authorizeStep,
      reserveBalanceStep,
      creditStep,
      completeStep,
      notifyStep
    );

    saga.execute(context);
  }
}
