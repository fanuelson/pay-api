package com.example.demo.application.chain.transfer.step;

import com.example.demo.application.chain.transfer.TransferContext;
import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadDataStep implements TransferHandler {

  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;
  private final WalletRepository walletRepository;

  @Override
  public String name() {
    return "LoadData";
  }

  @Override
  public void execute(TransferContext context) {
    var transaction = transactionRepository.findById(context.getTransactionId())
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

    context.setTransaction(transaction);
    context.setPayerId(payerId);
    context.setPayeeId(payeeId);
    context.setAmountInCents(transaction.getAmountInCents());
    context.setPayer(payer);
    context.setPayee(payee);
    context.setPayerWallet(payerWallet);
    context.setPayeeWallet(payeeWallet);
  }
}
