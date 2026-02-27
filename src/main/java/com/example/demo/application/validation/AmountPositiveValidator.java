package com.example.demo.application.validation;

import com.example.demo.domain.model.TransactionAggregate;
import org.springframework.stereotype.Service;

@Service
public class AmountPositiveValidator implements TransferValidator {

  @Override
  public ValidationResult validate(TransactionAggregate context) {
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
