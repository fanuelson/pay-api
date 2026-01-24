package com.example.demo.domain.model;

import com.example.demo.domain.helper.MoneyHelper;
import com.example.demo.domain.helper.DateTimeHelper;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Wallet {

  private Long id;
  private Long userId;
  private Long balanceInCents;
  private int version;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Wallet(Long userId, Long balanceInCents) {
    this.id = null;
    this.userId = userId;
    this.balanceInCents = balanceInCents;
    this.version = 0;
    this.createdAt = DateTimeHelper.now();
    this.updatedAt = DateTimeHelper.now();

    validate();
  }

  public Wallet(final Wallet other) {
    this.id = other.getId();
    this.userId = other.getUserId();
    this.balanceInCents = other.getBalanceInCents();
    this.version = other.getVersion();
    this.createdAt = other.getCreatedAt();
    this.updatedAt = other.getUpdatedAt();
    validate();
  }

  public static String formatCentsToReais(long cents) {
    long reais = cents / 100;
    long centavos = cents % 100;
    return String.format("R$ %d,%02d", reais, centavos);
  }

  public Wallet withId(final Long newId) {
    final var newWallet = new Wallet(this);
    newWallet.setId(newId);
    return newWallet;
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