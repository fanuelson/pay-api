package com.example.demo.application.transaction;

import com.example.demo.application.listener.TransactionCreatedListener;
import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.transaction.repository.TransactionRepository;
import com.example.demo.domain.wallet.WalletRepository;
import com.example.demo.infra.messaging.transaction.TransactionCompletedEvent;
import com.example.demo.infra.messaging.transaction.TransactionCreatedEvent;
import com.example.demo.infra.messaging.transaction.TransactionFailedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferUseCase implements TransactionCreatedListener {

  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;
  private final TransactionEventPublisher publisher;

  @Transactional
  public void handle(TransactionCreatedEvent event) {
    final var transactionId = event.getTransactionId();
    try {
      final var transaction = transactionRepository.findById(transactionId).orElseThrow();

      if (transaction.getAmountInCents() <= 0) {
        throw new BusinessException("A");
      }

      if (transaction.getPayerId().equals(transaction.getPayeeId())) {
        throw new BusinessException("B");
      }

      final var payerId = transaction.getPayerId();
      final var payeeId = transaction.getPayeeId();
      final var amountInCents = transaction.getAmountInCents();

      final var payerWallet = walletRepository.findByUserId(payerId).orElseThrow();
      final var payeeWallet = walletRepository.findByUserId(payeeId).orElseThrow();

      final var newPayerWallet = payerWallet.debit(amountInCents);
      final var newPayeeWallet = payeeWallet.credit(amountInCents);

      walletRepository.save(newPayerWallet);
      walletRepository.save(newPayeeWallet);
      publisher.publish(TransactionCompletedEvent.from(transactionId));
    } catch (BusinessException ex) {
      publisher.publish(TransactionFailedEvent.from(transactionId, ex.getMessage()));
      throw ex;
    }
  }
}
