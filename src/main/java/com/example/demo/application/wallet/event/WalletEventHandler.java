package com.example.demo.application.wallet.event;


public abstract class WalletEventHandler<E extends WalletEvent> {

  public abstract WalletEventResult handle(E event);

}
