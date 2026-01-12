package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String fullName;
    private String document;     // CPF ou CNPJ
    private String email;
    private UserType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Construtor equivalente ao Kotlin (com defaults e validações do init).
     */
    public User(String fullName, String document, String email, UserType type) {
        this.id = null;
        this.fullName = fullName;
        this.document = document;
        this.email = email;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        validate();
    }

    /**
     * Validações do bloco init do Kotlin
     */
    private void validate() {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Nome completo não pode estar vazio");
        }
        if (document == null || document.isBlank()) {
            throw new IllegalArgumentException("Documento não pode estar vazio");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode estar vazio");
        }
    }

    /* ==== Regras de domínio ==== */

    public boolean isMerchant() {
        return type == UserType.MERCHANT;
    }

    public boolean isCommon() {
        return type == UserType.COMMON;
    }

    public boolean canSendMoney() {
        return type == UserType.COMMON;
    }

    public boolean canTransfer() {
        return canSendMoney();
    }

    public boolean isNotAbleToTransfer() {
        return !canTransfer();
    }

    public boolean canReceiveMoney() {
        return true;
    }
}
