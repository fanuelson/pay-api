package com.example.demo.application.usecase;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.port.in.CreateTransactionOutput;
import com.example.demo.application.validator.CreateTransactionValidator;
import com.example.demo.domain.event.TransactionEventKey;
import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.infra.messaging.transfer.TransferEventKafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTransactionUseCase {

  private final TransactionRepository transactionRepository;
  private final TransferEventKafkaPublisher transferEventKafkaPublisher;
  private final CreateTransactionValidator createTransactionValidator;

  public CreateTransactionOutput execute(CreateTransactionCommand command) {
    createTransactionValidator.validate(command);
    var transaction = Transaction.pending(command.payerId(), command.payeeId(), command.amountInCents());
    transactionRepository.save(transaction);
    final var key = TransactionEventKey.of(command.payerId().toString());
    TransferEvent
      .of(key, TransactionEventType.REQUESTED, transaction.getId())
      .publish(transferEventKafkaPublisher::publish);

    return CreateTransactionOutput.of(transaction.getId());
  }
}
