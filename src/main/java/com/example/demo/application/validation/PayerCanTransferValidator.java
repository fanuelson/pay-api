package com.example.demo.application.validation;

import com.example.demo.domain.model.TransferContext;
import com.example.demo.domain.model.UserType;
import com.example.demo.domain.validation.TransferValidator;
import com.example.demo.domain.validation.ValidationError;
import com.example.demo.domain.validation.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class PayerCanTransferValidator implements TransferValidator {

  @Override
  public ValidationResult validate(TransferContext context) {

    if (context.getPayerUser().is(UserType.MERCHANT)) {
      return ValidationResult.invalid(
        ValidationError.of("TRANSFER_MERCHANT_CANNOT_SEND", "Merchants cannot send transfers", "payerId")
      );
    }

    return ValidationResult.valid();
  }
}