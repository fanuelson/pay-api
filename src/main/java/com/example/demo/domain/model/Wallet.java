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

    /**
     * Construtor equivalente ao Kotlin (com defaults e validações do init)
     */
    public Wallet(Long userId, Long balanceInCents) {
        this.id = null;
        this.userId = userId;
        this.balanceInCents = balanceInCents;
        this.version = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        validate();
    }

    /**
     * Validações do bloco init do Kotlin
     */
    private void validate() {
        if (balanceInCents < 0) {
            throw new IllegalArgumentException("Saldo não pode ser negativo");
        }
    }

    /**
     * Debita um valor da carteira
     *
     * @throws IllegalArgumentException se o saldo for insuficiente
     */
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

    /**
     * Credita um valor na carteira
     */
    public void credit(long amountInCents) {
        if (amountInCents <= 0) {
            throw new IllegalArgumentException("Valor de crédito deve ser maior que zero");
        }

        this.balanceInCents = MoneyHelper.add(this.balanceInCents, amountInCents);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica se há saldo insuficiente
     */
    public boolean hasInsufficientBalance(long amountInCents) {
        return this.balanceInCents < amountInCents;
    }

    /**
     * Retorna o saldo em reais para exibição
     */
    public String getBalanceInReais() {
        return formatCentsToReais(this.balanceInCents);
    }

    /**
     * Retorna o saldo como double (use com cuidado!)
     */
    public double getBalanceAsDouble() {
        return balanceInCents / 100.0;
    }

    /* ==== Métodos estáticos (equivalente ao companion object) ==== */

    /**
     * Formata centavos para string em reais
     */
    public static String formatCentsToReais(long cents) {
        long reais = cents / 100;
        long centavos = cents % 100;
        return String.format("R$ %d,%02d", reais, centavos);
    }

    /**
     * Converte reais (double) para centavos
     * CUIDADO: Double tem imprecisão! Use apenas para input do usuário
     */
    public static long reaisToCents(double reais) {
        return (long) (reais * 100);
    }
}