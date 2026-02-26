package com.example.demo.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

  private Long id;
  private String aggregateId;
  private String eventKey;
  private String aggregateType;
  private String eventType;
  private String topic;
  private String payload;
  private String payloadType;
  private OutboxEventStatus status;
  private int attempts;
  private int maxAttempts;
  private LocalDateTime createdAt;
  private LocalDateTime dispatchedAt;

  public String getEffectiveEventKey() {
    return eventKey != null ? eventKey : aggregateId;
  }

  public void dispatched() {
    this.status = OutboxEventStatus.DISPATCHED;
    this.dispatchedAt = LocalDateTime.now();
    this.attempts++;
  }

  public void resetToPending() {
    this.status = OutboxEventStatus.PENDING;
    this.dispatchedAt = null;
  }

  public void failed() {
    this.status = OutboxEventStatus.FAILED;
  }

  public boolean hasReachedMaxAttempts() {
    return this.attempts >= this.maxAttempts;
  }
}
