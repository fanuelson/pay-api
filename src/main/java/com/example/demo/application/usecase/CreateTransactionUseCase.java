package com.example.demo.application.usecase;

import com.example.demo.domain.event.TransactionRequestedEvent;
import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.port.in.CreateTransactionOutput;
import com.example.demo.application.port.out.event.TransactionEventPublisher;
import com.example.demo.application.validator.CreateTransactionValidator;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTransactionUseCase {

  private final TransactionRepository transactionRepository;
  private final TransactionEventPublisher transactionEventPublisher;
  private final CreateTransactionValidator createTransactionValidator;

  public CreateTransactionOutput execute(CreateTransactionCommand command) {
    createTransactionValidator.validate(command);
    var transaction = Transaction.pending(command.payerId(), command.payeeId(), command.amountInCents());
    transactionRepository.save(transaction);
    TransactionRequestedEvent
      .of(command.payerId().toString(), transaction.getId())
      .publish(transactionEventPublisher::publish);

    return CreateTransactionOutput.of(transaction.getId());
  }
}
