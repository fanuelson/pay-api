package com.example.demo.application.validation;

import com.example.demo.application.chain.transfer.TransferContext;
import org.springframework.stereotype.Service;

@Service
public class AmountPositiveValidator implements TransferValidator {

  @Override
  public ValidationResult validate(TransferContext context) {
    long amountInCents = context.getAmountInCents();

    if (amountInCents <= 0) {
      return ValidationResult.invalid(
        ValidationError.of(
          "TRANSFER_AMOUNT_MUST_BE_POSITIVE",
          "Transfer amount must be greater than zero",
          "amountInCents",
          amountInCents
        )
      );
    }

    return ValidationResult.valid();
  }
}
