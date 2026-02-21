package com.example.demo.application.wallet.command.credit;

import com.example.demo.domain.wallet.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditWalletCommandHandler {

  private final WalletRepository walletRepository;

  public CreditWalletCommandResult handle(CreditWalletCommand command) {


    return new CreditWalletCommandResult();
  }
}
