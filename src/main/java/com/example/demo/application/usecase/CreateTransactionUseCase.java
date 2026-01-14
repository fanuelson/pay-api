package com.example.demo.application.usecase;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.port.in.CreateTransactionOutput;
import com.example.demo.application.port.out.event.TransferEventPublisher;
import com.example.demo.application.port.out.service.LockService;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.TransactionStatus;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTransactionUseCase {

  private static final long LOCK_WAIT_TIME = 5L;
  private static final long LOCK_LEASE_TIME = 10L;

  private final LockService lockService;
  private final TransactionRepository transactionRepository;
  private final TransferEventPublisher transferEventPublisher;

  public CreateTransactionOutput execute(CreateTransactionCommand command) {
    try {
      return lockService.withLock(
        command.getPayerId().toString(), LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS,
        () -> create(command)
      );
    } catch (Exception e) {
      log.error("Lock");
      throw e;
    }
  }


  private CreateTransactionOutput create(CreateTransactionCommand command) {

    final var transactionId = UUID.randomUUID().toString();

    final var transaction = Transaction.builder()
      .id(transactionId)
      .payerId(command.getPayerId())
      .payeeId(command.getPayeeId())
      .status(TransactionStatus.PENDING)
      .amountInCents(command.getAmountInCents())
      .createdAt(LocalDateTime.now())
      .build();

    final var createdTransaction = transactionRepository.save(transaction);

    return CreateTransactionOutput.of(transactionId);
  }


}
