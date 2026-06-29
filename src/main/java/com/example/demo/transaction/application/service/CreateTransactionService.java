package com.example.demo.transaction.application.service;

import com.example.demo.shared.domain.TransactionId;
import com.example.demo.transaction.application.port.in.CreateTransactionUseCase;
import com.example.demo.transaction.application.port.in.command.CreateTransactionCommand;
import com.example.demo.transaction.application.port.out.TransactionRepository;
import com.example.demo.transaction.domain.Transaction;
import com.example.demo.wallet.application.port.in.CreditWalletUseCase;
import com.example.demo.wallet.application.port.in.DebitWalletUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {

  private final TransactionRepository repository;
  private final DebitWalletUseCase debitWallet;
  private final CreditWalletUseCase creditWallet;

  @Override
  @Transactional
  public TransactionId execute(CreateTransactionCommand command) {
    final var transaction = Transaction.create(
        command.amountInCents(),
        command.payerId(),
        command.payeeId()
    );

    debitWallet.debit(new DebitWalletUseCase.Command(command.payerId(), command.amountInCents()));
    creditWallet.credit(new CreditWalletUseCase.Command(command.payeeId(), command.amountInCents()));

    return repository.create(transaction);
  }
}
