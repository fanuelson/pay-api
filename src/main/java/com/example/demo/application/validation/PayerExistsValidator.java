package com.example.demo.application.validation;

import com.example.demo.domain.model.TransactionAggregate;
import com.example.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PayerExistsValidator implements TransferValidator {

  private final UserRepository userRepository;

  @Override
  public ValidationResult validate(final TransactionAggregate context) {
    var errors = new ArrayList<ValidationError>();
    userRepository.findById(context.getPayerId()).ifPresentOrElse(
      context::setPayer,
      () -> errors.add(ValidationError.of("TRANSFER_PAYER_NOT_FOUND", "Payer not found", "payerId"))
    );
    return ValidationResult.of(errors);
  }
}
