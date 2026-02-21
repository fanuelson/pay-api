package com.example.demo.application.wallet.command.credit;

import com.example.demo.domain.wallet.WalletId;


public record CreditWalletCommand(
  WalletId walletId,
  long amountInCents
) {
}
