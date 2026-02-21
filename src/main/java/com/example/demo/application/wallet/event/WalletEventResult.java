package com.example.demo.application.wallet.event;

import com.example.demo.domain.wallet.WalletId;


public abstract class WalletEventResult extends WalletEvent {

  protected WalletEventResult(WalletId walletId) {
    super(walletId);
  }
}
