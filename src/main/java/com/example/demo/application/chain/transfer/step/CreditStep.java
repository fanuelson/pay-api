package com.example.demo.application.chain.transfer.step;

import com.example.demo.application.chain.transfer.TransferContext;
import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.domain.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditStep implements TransferHandler {

  private final WalletRepository walletRepository;

  @Override
  public String name() {
    return "Credit";
  }

  @Override
  @Transactional
  public void execute(TransferContext context) {
    var payerWallet = context.getPayerWallet();
    var payeeWallet = context.getPayeeWallet();
    final var amount = context.getAmountInCents();
    payerWallet.debit(amount);
    payeeWallet.credit(amount);
    walletRepository.update(payerWallet.getId(), payerWallet);
    walletRepository.update(payeeWallet.getId(), payeeWallet);
  }

  @Override
  @Transactional
  public void compensate(TransferContext context, Exception cause) {
    var payerWallet = context.getPayerWallet();
    var payeeWallet = context.getPayeeWallet();
    final var amount = context.getAmountInCents();
    payerWallet.credit(amount);
    payeeWallet.debit(amount);
    walletRepository.update(payerWallet.getId(), payerWallet);
    walletRepository.update(payeeWallet.getId(), payeeWallet);
  }
}
