package com.example.demo.application.saga.transfer.step;

import com.example.demo.application.saga.SagaStep;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.application.validation.TransferContext;
import com.example.demo.application.validation.TransferValidator;
import com.example.demo.domain.exception.BusinessValidationException;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ValidateStep implements SagaStep<TransferSagaContext> {

  private final List<TransferValidator> validators;
  private final TransactionRepository transactionRepository;

  @Override
  public String getName() {
    return "Validate";
  }

  @Override
  public void execute(TransferSagaContext context) {
    var transferContext = new TransferContext(
        context.getPayerId(),
        context.getPayeeId(),
        context.getAmountInCents(),
        context.getPayer(),
        context.getPayee(),
        context.getPayerWallet(),
        context.getPayeeWallet()
    );

    validators.forEach(validator -> {
      var result = validator.validate(transferContext);
      if (result.isInvalid()) {
        throw new BusinessValidationException(result.getErrors());
      }
    });
  }

  @Override
  public void compensate(TransferSagaContext context, String cause) {
    context.getTransaction().failed(cause);
    transactionRepository.save(context.getTransaction());
  }
}
