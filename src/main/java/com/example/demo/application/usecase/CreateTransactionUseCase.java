package com.example.demo.application.usecase;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.port.in.CreateTransactionOutput;
import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.port.out.event.TransferEventPublisher;
import com.example.demo.application.port.out.event.TransferEventType;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.TransactionStatus;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.helper.DateTimeHelper;
import com.example.demo.domain.vo.TransactionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateTransactionUseCase {

  private final TransactionRepository transactionRepository;
  private final TransferEventPublisher transferEventPublisher;

  public CreateTransactionOutput execute(CreateTransactionCommand command) {
    var transactionId = TransactionId.generate();

    var transaction = Transaction.builder()
        .id(transactionId)
        .payerId(command.payerId())
        .payeeId(command.payeeId())
        .status(TransactionStatus.PENDING)
        .amountInCents(command.amountInCents())
        .createdAt(DateTimeHelper.now())
        .build();

    transactionRepository.save(transaction);

    var event = new TransferEvent(
        transactionId,
        command.payerId(),
        command.payeeId(),
        command.amountInCents(),
        TransactionStatus.PENDING.name(),
        TransferEventType.REQUESTED,
        null
    );

    transferEventPublisher.publish(event);

    return CreateTransactionOutput.of(transactionId);
  }
}
