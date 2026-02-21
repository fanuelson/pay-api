package com.example.demo.application.wallet.command.debit;

import com.example.demo.domain.wallet.WalletId;


public record DebitWalletCommand(
  WalletId walletId,
  long amountInCents
) {
}
