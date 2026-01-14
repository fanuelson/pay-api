package com.example.demo.application.validation;

import com.example.demo.domain.model.TransferContext;
import com.example.demo.domain.port.repository.UserRepository;
import com.example.demo.domain.validation.TransferValidator;
import com.example.demo.domain.validation.ValidationError;
import com.example.demo.domain.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PayeeExistsValidator implements TransferValidator {

  private final UserRepository userRepository;

  @Override
  public ValidationResult validate(final TransferContext context) {
    var errors = new ArrayList<ValidationError>();
    userRepository.findById(context.getPayeeId()).ifPresentOrElse(
      context::setPayeeUser,
      () -> errors.add(ValidationError.of("TRANSFER_PAYEE_NOT_FOUND", "Payee not found", "payeeId"))
    );
    return ValidationResult.of(errors);
  }
}
