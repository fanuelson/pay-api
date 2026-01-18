package com.example.demo.application.usecase;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.port.in.CreateTransactionOutput;
import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.port.out.event.TransferEventPublisher;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.TransactionStatus;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateTransactionUseCase {

  private final TransactionRepository transactionRepository;
  private final TransferEventPublisher transferEventPublisher;

  public CreateTransactionOutput execute(CreateTransactionCommand command) {
    var transactionId = UUID.randomUUID().toString();

    var transaction = Transaction.builder()
        .id(transactionId)
        .payerId(command.payerId())
        .payeeId(command.payeeId())
        .status(TransactionStatus.PENDING)
        .amountInCents(command.amountInCents())
        .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .build();

    transactionRepository.save(transaction);

    var event = new TransferEvent(
        transactionId,
        command.payerId(),
        command.payeeId(),
        command.amountInCents(),
        TransactionStatus.PENDING.name(),
        null
    );

    transferEventPublisher.publish(event);

    return CreateTransactionOutput.of(transactionId);
  }
}
