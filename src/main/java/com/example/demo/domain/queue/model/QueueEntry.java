package com.example.demo.domain.queue.model;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class QueueEntry {

  private String id;
  private String userId;
  private String targetId;
  private long position;
  private QueueEntryStatus status;
  private LocalDateTime joinedAt;
  private LocalDateTime calledAt;
  private LocalDateTime expiresAt;

  public enum QueueEntryStatus {
    WAITING, CALLED, EXPIRED, COMPLETED
  }
}
