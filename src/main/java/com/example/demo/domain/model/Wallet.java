package com.example.demo.domain.model;

import com.example.demo.domain.exception.InsufficientBalanceException;
import com.example.demo.domain.helper.MoneyHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

  private Long id;
  private Long userId;
  private Long balanceInCents;
  private Long reservedBalanceInCents;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Wallet(Long userId, Long balanceInCents) {
    this.id = null;
    this.userId = userId;
    this.balanceInCents = balanceInCents;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();

    validate();
  }

  public Wallet(final Wallet other) {
    this.id = other.getId();
    this.userId = other.getUserId();
    this.balanceInCents = other.getBalanceInCents();
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

  public void reserve(long amountInCents) {
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Valor de reserva deve ser maior que zero");
    }

    if (balanceInCents < amountInCents) {
      throw new InsufficientBalanceException(
        "Saldo insuficiente. Saldo disponível: " + getBalanceInReais() +
          ", Valor solicitado: " + formatCentsToReais(amountInCents)
      );
    }

    this.balanceInCents = MoneyHelper.subtract(this.balanceInCents, amountInCents);
    this.reservedBalanceInCents = MoneyHelper.add(this.reservedBalanceInCents, amountInCents);
    this.updatedAt = LocalDateTime.now();
  }

  public void releaseReserve(long amountInCents) {
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Valor de liberação deve ser maior que zero");
    }

    if (reservedBalanceInCents < amountInCents) {
      throw new IllegalArgumentException("Saldo reservado insuficiente para liberação");
    }

    this.reservedBalanceInCents = MoneyHelper.subtract(this.reservedBalanceInCents, amountInCents);
    this.balanceInCents = MoneyHelper.add(this.balanceInCents, amountInCents);
    this.updatedAt = LocalDateTime.now();
  }

  public void debit(long amountInCents) {
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Valor de débito deve ser maior que zero");
    }

    if (reservedBalanceInCents < amountInCents) {
      throw new IllegalArgumentException("Saldo reservado insuficiente para débito");
    }

    this.reservedBalanceInCents = MoneyHelper.subtract(this.reservedBalanceInCents, amountInCents);
    this.updatedAt = LocalDateTime.now();
  }

  public void credit(long amountInCents) {
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Valor de crédito deve ser maior que zero");
    }

    this.balanceInCents = MoneyHelper.add(this.balanceInCents, amountInCents);
    this.updatedAt = LocalDateTime.now();
  }

  public boolean hasInsufficientBalance(long amountInCents) {
    return this.balanceInCents < amountInCents;
  }

  public String getBalanceInReais() {
    return formatCentsToReais(this.balanceInCents);
  }

}
