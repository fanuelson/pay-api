package com.example.demo.domain.repository;

import com.example.demo.domain.model.OutboxEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxEventRepository {
  OutboxEvent save(OutboxEvent outboxEvent);
  OutboxEvent update(OutboxEvent outboxEvent);
  List<OutboxEvent> findPending(int limit);
  List<OutboxEvent> findDispatchedBefore(LocalDateTime threshold, int limit);
}
