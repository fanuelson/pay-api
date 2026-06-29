package com.example.demo.wallet.application.service;

import com.example.demo.shared.domain.exception.NotFound;
import com.example.demo.wallet.application.port.in.CreditWalletUseCase;
import com.example.demo.wallet.application.port.out.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreditWalletService implements CreditWalletUseCase {

  private final WalletRepository wallets;

  @Override
  @Transactional
  public void credit(Command command) {
    final var wallet = wallets.findByUserId(command.userId())
        .orElseThrow(NotFound.of("Wallet", command.userId().value()));
    wallets.update(wallet.credit(command.amountInCents()));
  }
}
