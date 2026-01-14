package com.example.demo.application.validation;

import com.example.demo.domain.model.TransferContext;
import com.example.demo.domain.validation.TransferValidator;
import com.example.demo.domain.validation.ValidationError;
import com.example.demo.domain.validation.ValidationResult;

public class SelfTransferValidator implements TransferValidator {

  @Override
  public ValidationResult validate(TransferContext context) {
    if (context.getPayerId().equals(context.getPayeeId())) {
      return ValidationResult.invalid(
        ValidationError.of("TRANSFER_SELF_TRANSFER_NOT_ALLOWED", "Payee not found", "payeeId")
      );
    }
    return ValidationResult.valid();
  }

}
