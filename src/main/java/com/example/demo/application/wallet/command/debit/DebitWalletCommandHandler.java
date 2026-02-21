package com.example.demo.application.wallet.command.debit;

import com.example.demo.domain.wallet.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebitWalletCommandHandler {

  private final WalletRepository walletRepository;

  public DebitWalletCommandResult handle(DebitWalletCommand command) {

    return new DebitWalletCommandResult();
  }
}
