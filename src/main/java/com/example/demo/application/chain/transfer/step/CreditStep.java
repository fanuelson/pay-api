package com.example.demo.application.chain.transfer.step;

import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.domain.model.TransactionAggregate;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditStep implements TransferHandler {

  private final WalletRepository walletRepository;
  private final TransactionRepository transactionRepository;

  @Override
  public String name() {
    return "Credit";
  }

  @Override
  @Transactional
  public void execute(TransactionAggregate context) {
    var payerWallet = context.getPayerWallet();
    var payeeWallet = context.getPayeeWallet();
    final var amount = context.getAmountInCents();
    payerWallet.debit(amount);
    payeeWallet.credit(amount);
    walletRepository.update(payerWallet.getId(), payerWallet);
    walletRepository.update(payeeWallet.getId(), payeeWallet);
    context.getTransaction().completed();
    transactionRepository.update(context.getTransactionId(), context.getTransaction());
  }

  @Override
  @Transactional
  public void compensate(TransactionAggregate context, Exception cause) {
    var payerWallet = context.getPayerWallet();
    var payeeWallet = context.getPayeeWallet();
    final var amount = context.getAmountInCents();
    payerWallet.credit(amount);
    payeeWallet.debit(amount);
    walletRepository.update(payerWallet.getId(), payerWallet);
    walletRepository.update(payeeWallet.getId(), payeeWallet);
    context.getTransaction().failed(cause.getMessage());
    transactionRepository.update(context.getTransactionId(), context.getTransaction());
  }
}
