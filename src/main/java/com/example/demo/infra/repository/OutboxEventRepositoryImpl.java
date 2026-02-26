package com.example.demo.infra.repository;

import com.example.demo.domain.model.OutboxEvent;
import com.example.demo.domain.repository.OutboxEventRepository;
import com.example.demo.infra.repository.jpa.OutboxEventJpaRepository;
import com.example.demo.infra.repository.jpa.entities.OutboxEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class OutboxEventRepositoryImpl implements OutboxEventRepository {

  private final OutboxEventJpaRepository repository;

  @Override
  public OutboxEvent save(OutboxEvent outboxEvent) {
    return toModel(repository.save(toEntity(outboxEvent)));
  }

  @Override
  public OutboxEvent update(OutboxEvent outboxEvent) {
    var entity = repository.findById(outboxEvent.getId()).orElseThrow();
    entity.setStatus(outboxEvent.getStatus());
    entity.setAttempts(outboxEvent.getAttempts());
    entity.setDispatchedAt(outboxEvent.getDispatchedAt());
    return toModel(repository.save(entity));
  }

  @Override
  public List<OutboxEvent> findPending(int limit) {
    return repository.findPending(PageRequest.of(0, limit))
        .stream().map(this::toModel).toList();
  }

  @Override
  public List<OutboxEvent> findDispatchedBefore(LocalDateTime threshold, int limit) {
    return repository.findDispatchedBefore(threshold, PageRequest.of(0, limit))
        .stream().map(this::toModel).toList();
  }

  private OutboxEventEntity toEntity(OutboxEvent model) {
    return OutboxEventEntity.builder()
        .id(model.getId())
        .aggregateId(model.getAggregateId())
        .eventKey(model.getEventKey())
        .aggregateType(model.getAggregateType())
        .eventType(model.getEventType())
        .topic(model.getTopic())
        .payload(model.getPayload())
        .payloadType(model.getPayloadType())
        .status(model.getStatus())
        .attempts(model.getAttempts())
        .maxAttempts(model.getMaxAttempts())
        .createdAt(model.getCreatedAt())
        .dispatchedAt(model.getDispatchedAt())
        .build();
  }

  private OutboxEvent toModel(OutboxEventEntity entity) {
    return OutboxEvent.builder()
        .id(entity.getId())
        .aggregateId(entity.getAggregateId())
        .eventKey(entity.getEventKey())
        .aggregateType(entity.getAggregateType())
        .eventType(entity.getEventType())
        .topic(entity.getTopic())
        .payload(entity.getPayload())
        .payloadType(entity.getPayloadType())
        .status(entity.getStatus())
        .attempts(entity.getAttempts())
        .maxAttempts(entity.getMaxAttempts())
        .createdAt(entity.getCreatedAt())
        .dispatchedAt(entity.getDispatchedAt())
        .build();
  }
}
