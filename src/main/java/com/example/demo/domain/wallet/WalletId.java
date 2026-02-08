package com.example.demo.domain.wallet;

import static java.util.Objects.requireNonNull;

public record WalletId(String value) {

  public WalletId {
    requireNonNull(value);
  }

  public static WalletId of(String value) {
    return new WalletId(value);
  }

  public Long asLong() {
    return Long.valueOf(value);
  }
}
