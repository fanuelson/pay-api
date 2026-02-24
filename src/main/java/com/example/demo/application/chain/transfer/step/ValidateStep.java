package com.example.demo.application.chain.transfer.step;

import com.example.demo.application.chain.transfer.TransferContext;
import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.application.validation.TransferValidator;
import com.example.demo.domain.exception.BusinessValidationException;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ValidateStep implements TransferHandler {

  private final List<TransferValidator> validators;
  private final TransactionRepository transactionRepository;

  @Override
  public String name() {
    return "Validate";
  }

  @Override
  public void execute(TransferContext context) {
    validators.forEach(validator -> {
      var result = validator.validate(context);
      if (result.isInvalid()) {
        throw new BusinessValidationException(result.getErrors());
      }
    });
  }

  @Override
  public void compensate(TransferContext context, Exception cause) {
    context.getTransaction().failed(cause.getMessage());
    transactionRepository.save(context.getTransaction());
  }
}
