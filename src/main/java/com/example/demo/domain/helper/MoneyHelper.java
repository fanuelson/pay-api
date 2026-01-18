package com.example.demo.domain.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyHelper {

  private static final long CENTS_PER_UNIT = 100L;

  public static long add(long cents1, long cents2) {
    return cents1 + cents2;
  }

  public static long subtract(long cents1, long cents2) {
    return cents1 - cents2;
  }

  public static long multiply(long cents, long quantity) {
    return cents * quantity;
  }

  public static String centsToString(long cents) {
    long reais = cents / CENTS_PER_UNIT;
    long centavos = cents % CENTS_PER_UNIT;
    return String.format("R$ %d,%02d", reais, centavos);
  }
}
