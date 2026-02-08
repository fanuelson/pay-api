package com.example.demo.infra.repository.notification.jpa.entities;

import com.example.demo.domain.notification.model.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notification_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventEntity {

  @Id
  private String id;

  @Column(name = "notification_id", nullable = false)
  private Long notificationId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 200)
  private NotificationStatus status;

  @Column(name = "error_message", columnDefinition = "TEXT")
  private String errorMessage;

  @Column(name = "attempts", nullable = false)
  private Long attempts;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

}