package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private String id;
    private Long payerId;
    private Long payeeId;
    private Long amountInCents;
    private TransactionStatus status;
    private String authorizationCode;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    /**
     * Construtor equivalente ao Kotlin (com defaults e validações do init).
     */
    public Transaction(
            Long payerId,
            Long payeeId,
            Long amountInCents
    ) {
        this.id = UUID.randomUUID().toString();
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.amountInCents = amountInCents;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();

        validate();
    }

    /**
     * Validações do bloco init do Kotlin
     */
    private void validate() {
        if (payerId.equals(payeeId)) {
            throw new IllegalArgumentException(
                    "Pagador e recebedor não podem ser o mesmo usuário"
            );
        }
        if (amountInCents <= 0) {
            throw new IllegalArgumentException(
                    "Valor da transação deve ser maior que zero"
            );
        }
    }

    /* ==== Regras de domínio ==== */

    public String getAmountInReais() {
        long reais = amountInCents / 100;
        long centavos = amountInCents % 100;
        return String.format("R$ %d,%02d", reais, centavos);
    }

    public void authorize(String code) {
        if (status != TransactionStatus.PENDING) {
            throw new IllegalStateException(
                    "Apenas transações PENDING podem ser autorizadas"
            );
        }
        this.status = TransactionStatus.AUTHORIZED;
        this.authorizationCode = code;
    }

    public void complete() {
        if (status != TransactionStatus.AUTHORIZED &&
                status != TransactionStatus.PENDING) {
            throw new IllegalStateException(
                    "Apenas transações AUTHORIZED ou PENDING podem ser completadas"
            );
        }
        this.status = TransactionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void fail(String reason) {
        this.status = TransactionStatus.FAILED;
        this.errorMessage = reason;
        this.completedAt = LocalDateTime.now();
    }

    public void reverse(String reason) {
        if (status != TransactionStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Apenas transações COMPLETED podem ser revertidas"
            );
        }
        this.status = TransactionStatus.REVERSED;
        this.errorMessage = reason;
    }

    public void markAsCompleted(String authCode) {
        this.status = TransactionStatus.COMPLETED;
        this.authorizationCode = authCode;
        this.completedAt = LocalDateTime.now();
    }

    public void markAsFailed(String error) {
        this.status = TransactionStatus.FAILED;
        this.errorMessage = error;
        this.completedAt = LocalDateTime.now();
    }

    /* ==== Helpers de estado ==== */

    public boolean isNotCompleted() {
        return status != TransactionStatus.COMPLETED;
    }

    public boolean isSuccessful() {
        return status == TransactionStatus.COMPLETED;
    }

    public boolean isFailed() {
        return status == TransactionStatus.FAILED;
    }

    public boolean isPending() {
        return status == TransactionStatus.PENDING;
    }
}