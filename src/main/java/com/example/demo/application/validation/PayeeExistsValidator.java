package com.example.demo.application.validation;

import com.example.demo.domain.model.TransactionAggregate;
import com.example.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PayeeExistsValidator implements TransferValidator {

  private final UserRepository userRepository;

  @Override
  public ValidationResult validate(final TransactionAggregate context) {
    var errors = new ArrayList<ValidationError>();
    userRepository.findById(context.getPayeeId()).ifPresentOrElse(
      context::setPayee,
      () -> errors.add(ValidationError.of("TRANSFER_PAYEE_NOT_FOUND", "Payee not found", "payeeId"))
    );
    return ValidationResult.of(errors);
  }
}
