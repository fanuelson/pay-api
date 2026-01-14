package com.example.demo.domain.model;

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
    private int version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wallet(Long userId, Long balanceInCents) {
        this.id = null;
        this.userId = userId;
        this.balanceInCents = balanceInCents;
        this.version = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

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
                    "Saldo insuficiente. Saldo atual: " + getBalanceInReais() +
                            ", Valor solicitado: " + formatCentsToReais(amountInCents)
            );
        }

        this.balanceInCents = MoneyHelper.subtract(this.balanceInCents, amountInCents);
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

    public static String formatCentsToReais(long cents) {
        long reais = cents / 100;
        long centavos = cents % 100;
        return String.format("R$ %d,%02d", reais, centavos);
    }

}