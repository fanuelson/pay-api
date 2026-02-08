package com.example.demo.domain.transaction.validation;

import org.springframework.stereotype.Service;

@Service
public class SelfTransferValidator implements TransferValidator {

  @Override
  public ValidationResult validate(TransferContext context) {
    if (context.getPayerId().equals(context.getPayeeId())) {
      return ValidationResult.invalid(
        ValidationError.of("TRANSFER_SELF_TRANSFER_NOT_ALLOWED", "Self transfer not allowed", "payeeId")
      );
    }
    return ValidationResult.valid();
  }

}
