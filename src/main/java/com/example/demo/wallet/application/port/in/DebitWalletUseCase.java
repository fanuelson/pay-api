package com.example.demo.wallet.application.port.in;

import com.example.demo.shared.domain.UserId;

public interface DebitWalletUseCase {

  void debit(Command command);

  record Command(UserId userId, long amountInCents) {
  }
}
