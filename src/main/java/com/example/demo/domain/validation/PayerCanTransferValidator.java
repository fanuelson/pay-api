package com.example.demo.domain.validation;

import com.example.demo.domain.model.UserType;
import org.springframework.stereotype.Component;

@Component
public class PayerCanTransferValidator implements TransferValidator {

  @Override
  public ValidationResult validate(TransferContext context) {

    if (context.getPayerUser().is(UserType.MERCHANT)) {
      return ValidationResult.invalid(
        ValidationError.of("TRANSFER_MERCHANT_CANNOT_SEND", "Merchants cannot transfer", "payerId")
      );
    }

    return ValidationResult.valid();
  }
}