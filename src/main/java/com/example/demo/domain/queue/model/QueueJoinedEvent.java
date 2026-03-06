package com.example.demo.domain.queue.model;

import java.time.LocalDateTime;

public record QueueJoinedEvent(
  String userId,
  String eventId,
  int position,
  LocalDateTime joinedAt
) {}
