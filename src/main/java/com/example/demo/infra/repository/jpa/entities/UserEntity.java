package com.example.demo.infra.repository.jpa.entities;

import com.example.demo.domain.model.NotificationChannel;
import com.example.demo.domain.model.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
  name = "users",
  indexes = {
    @Index(name = "idx_user_document", columnList = "document"),
    @Index(name = "idx_user_email", columnList = "email")
  }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "full_name", nullable = false, length = 255)
  private String fullName;

  @Column(name = "document", nullable = false, unique = true, length = 14)
  private String document;

  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @Convert(converter = EnabledNotificationChannelsConverter.class)
  @Column(name = "enabled_notification_channels", nullable = false, length = 20)
  private Set<NotificationChannel> enabledNotificationChannels;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type", nullable = false, length = 20)
  private UserType type;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

}