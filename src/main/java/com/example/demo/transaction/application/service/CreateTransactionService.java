package com.example.demo.transaction.application.service;

import com.example.demo.shared.domain.TransactionId;
import com.example.demo.transaction.application.port.in.CreateTransactionUseCase;
import com.example.demo.transaction.application.port.in.command.CreateTransactionCommand;
import com.example.demo.transaction.application.port.out.TransactionRepository;
import com.example.demo.transaction.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {

  private final TransactionRepository repository;

  @Override
  public TransactionId execute(CreateTransactionCommand command) {
    final var transaction = Transaction.create(
        command.amountInCents(),
        command.payerId(),
        command.payeeId()
    );
    return repository.create(transaction);
  }
}
