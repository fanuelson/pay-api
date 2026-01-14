package com.example.demo.application.saga.transfer.step;

import com.example.demo.application.saga.SagaStep;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompleteStep implements SagaStep<TransferSagaContext> {

  private final TransactionRepository transactionRepository;

  @Override
  public String getName() {
    return "Complete";
  }

  @Override
  public void execute(TransferSagaContext context) {
    var transaction = context.getTransaction();
    transaction.completed();
    transactionRepository.save(transaction);
  }

  @Override
  public void compensate(TransferSagaContext context, Exception cause) {
  }
}
