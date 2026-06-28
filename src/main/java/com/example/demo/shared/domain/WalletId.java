package com.example.demo.shared.domain;

public record WalletId(Long value) {
  public WalletId {
  }

  public static WalletId empty() {
    return new WalletId(null);
  }

  public static WalletId from(Long value) {
    return new WalletId(value);
  }
}
