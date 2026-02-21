package com.example.demo.application.wallet.event;

import com.example.demo.domain.wallet.WalletId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class WalletEvent {

  protected final WalletId walletId;

}
