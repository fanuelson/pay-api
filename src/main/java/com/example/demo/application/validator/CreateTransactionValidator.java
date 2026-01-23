package com.example.demo.application.validator;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.domain.exception.BusinessValidationException;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.repository.WalletRepository;
import com.example.demo.domain.validation.TransferContext;
import com.example.demo.domain.validation.TransferValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTransactionValidator {

  private final UserRepository userRepository;
  private final WalletRepository walletRepository;
  private final List<TransferValidator> validators;

  public void validate(CreateTransactionCommand command) {
    final var payer = userRepository.findById(command.payerId()).orElseThrow();
    final var payee = userRepository.findById(command.payeeId()).orElseThrow();

    final var payerWallet = walletRepository.findByUserId(command.payerId()).orElseThrow();
    final var payeeWallet = walletRepository.findByUserId(command.payeeId()).orElseThrow();

    var transferContext = new TransferContext(
      command.payerId(),
      command.payeeId(),
      command.amountInCents(),
      payer,
      payee,
      payerWallet,
      payeeWallet
    );
    validators.forEach(validator -> {
      var result = validator.validate(transferContext);
      if (result.isInvalid()) {
        throw new BusinessValidationException(result.getErrors());
      }
    });
  }
}
