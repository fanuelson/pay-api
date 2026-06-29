package com.example.demo.wallet.application.port.in;

import com.example.demo.shared.domain.UserId;

public interface CreditWalletUseCase {

  void credit(Command command);

  record Command(UserId userId, long amountInCents) {
  }
}
