package com.example.demo.application.validation;

import com.example.demo.domain.model.UserType;
import com.example.demo.domain.port.repository.UserRepository;
import com.example.demo.domain.validation.TransferContext;
import com.example.demo.domain.validation.TransferValidator;
import com.example.demo.domain.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTypeValidator implements TransferValidator {

  private final UserRepository userRepository;

  @Override
  public ValidationResult validate(TransferContext context) {
    if (isNull(context.getPayerUser())) {
      userRepository.findById(context.getPayerId()).ifPresent(context::setPayerUser);
    }

    if (isNull(context.getPayerUser())) {
      return ValidationResult.invalid("TRANSFER_PAYER_NOT_FOUND", "Payer not found", "payerId");
    }

    if (context.getPayerUser().getType() == UserType.MERCHANT) {
      return ValidationResult.invalid(
        "TRANSFER_MERCHANT_CANNOT_SEND", "Merchants cannot send transfers", "payerId"
      );
    }

    if (isNull(context.getPayeeUser())) {
      userRepository.findById(context.getPayeeId()).ifPresent(context::setPayeeUser);
    }

    if (isNull(context.getPayeeUser())) {
      return ValidationResult.invalid(
          "TRANSFER_SELF_TRANSFER_NOT_ALLOWED", "Payee not found", "payeeId"
      );
    }

    return ValidationResult.valid();
  }
}