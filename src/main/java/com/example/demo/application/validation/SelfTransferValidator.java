package com.example.demo.application.validation;

import com.example.demo.application.chain.transfer.TransferContext;

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
