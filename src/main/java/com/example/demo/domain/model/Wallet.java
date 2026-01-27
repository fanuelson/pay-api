package com.example.demo.domain.model;

import com.example.demo.domain.helper.DateTimeHelper;
import com.example.demo.domain.helper.MoneyHelper;
import com.example.demo.domain.vo.WalletId;
import lombok.*;
import java.time.LocalDateTime;

@Data
@With
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Wallet {

  private WalletId id;
  private Long userId;
  private Long balanceInCents;
  private int version;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static String formatCentsToReais(long cents) {
    long reais = cents / 100;
    long centavos = cents % 100;
    return String.format("R$ %d,%02d", reais, centavos);
  }

  private void validate() {
    if (balanceInCents < 0) {
      throw new IllegalArgumentException("Saldo não pode ser negativo");
    }
  }

  public void debit(long amountInCents) {
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Valor de débito deve ser maior que zero");
    }

    if (balanceInCents < amountInCents) {
      throw new IllegalArgumentException(
        "Saldo insuficiente. Saldo atual: " + getBalanceFormatted() +
          ", Valor solicitado: " + formatCentsToReais(amountInCents)
      );
    }

    this.balanceInCents = MoneyHelper.subtract(this.balanceInCents, amountInCents);
    this.updatedAt = DateTimeHelper.now();
  }

  public void credit(long amountInCents) {
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Valor de crédito deve ser maior que zero");
    }

    this.balanceInCents = MoneyHelper.add(this.balanceInCents, amountInCents);
    this.updatedAt = DateTimeHelper.now();
  }

  public boolean hasInsufficientBalance(long amountInCents) {
    return this.balanceInCents < amountInCents;
  }

  public String getBalanceFormatted() {
    return formatCentsToReais(this.balanceInCents);
  }

}