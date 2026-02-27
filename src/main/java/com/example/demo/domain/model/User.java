package com.example.demo.domain.model;

import com.example.demo.domain.notification.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

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
    private Set<NotificationChannel> enabledNotificationChannels;
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

    public boolean is(final UserType userType) { return userType.equals(getType()); }
}
