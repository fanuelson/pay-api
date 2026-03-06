package com.example.demo.domain.queue.model;

import java.time.LocalDateTime;

public record QueueCalledEvent(
  String userId,
  String eventId,
  LocalDateTime calledAt,
  LocalDateTime expiresAt    // 5 minutos para comprar
) {}
