package com.example.demo.domain.user.model;

import com.example.demo.domain.notification.model.NotificationChannel;
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

  private UserId id;
  private String fullName;
  private String document;     // CPF ou CNPJ
  private String email;
  private UserType type;
  private Set<NotificationChannel> enabledNotificationChannels;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

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

}
