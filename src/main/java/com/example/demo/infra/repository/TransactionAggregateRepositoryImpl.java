package com.example.demo.infra.repository;

import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.model.TransactionAggregate;
import com.example.demo.domain.repository.TransactionAggregateRepository;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionAggregateRepositoryImpl implements TransactionAggregateRepository {

  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;
  private final WalletRepository walletRepository;

  @Override
  public TransactionAggregate findById(String transactionId) {
    var transaction = transactionRepository.findById(transactionId)
      .orElseThrow(() -> new ElementNotFoundException("Transaction not found"));

    var payerId = transaction.getPayerId();
    var payeeId = transaction.getPayeeId();

    var payer = userRepository.findById(payerId)
      .orElseThrow(() -> new ElementNotFoundException("Payer not found"));

    var payee = userRepository.findById(payeeId)
      .orElseThrow(() -> new ElementNotFoundException("Payee not found"));

    var payerWallet = walletRepository.findByUserId(payerId)
      .orElseThrow(() -> new ElementNotFoundException("Payer wallet not found"));

    var payeeWallet = walletRepository.findByUserId(payeeId)
      .orElseThrow(() -> new ElementNotFoundException("Payee wallet not found"));

    return TransactionAggregate.builder()
      .transactionId(transactionId)
      .transaction(transaction)
      .payerId(payerId)
      .payeeId(payeeId)
      .amountInCents(transaction.getAmountInCents())
      .payer(payer)
      .payee(payee)
      .payerWallet(payerWallet)
      .payeeWallet(payeeWallet)
      .build();

  }
}
