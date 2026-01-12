package com.example.demo.infra.persistence.repository;

import com.example.demo.domain.model.Notification;
import com.example.demo.domain.port.repository.NotificationRepository;
import com.example.demo.infra.persistence.mapper.NotificationMapper;
import com.example.demo.infra.persistence.repository.jpa.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

  private final NotificationJpaRepository repository;
  private final NotificationMapper mapper;

  @Override
  public Notification save(Notification notification) {
    log.debug("Saving notification for transaction {}, recipient {}",
      notification.getTransactionId(), notification.getRecipientId());

    var entity = mapper.toEntity(notification);
    var savedEntity = repository.save(entity);

    log.info("Notification saved successfully - id: {}, Transaction: {}, Status: {}",
      savedEntity.getId(), savedEntity.getTransactionId(), savedEntity.getStatus());

    return mapper.toDomain(savedEntity);
  }

  @Override
  public Notification update(Long id, Notification notification) {
    log.debug("Updating notification: {}", id);

    var existingEntity = repository.findById(id)
      .orElseThrow(() -> {
        log.error("Notification not found for update: {}", id);
        return new IllegalArgumentException("Notification with ID=[" + id + "] not found");
      });

    mapper.updateEntity(existingEntity, notification);
    var savedEntity = repository.save(existingEntity);

    log.info("Notification updated successfully - ID: {}, New Status: {}, Attempts: {}",
      id, savedEntity.getStatus(), savedEntity.getAttempts());

    return mapper.toDomain(savedEntity);
  }

  @Override
  public void delete(Long id) {
    log.debug("Deleting notification: {}", id);

    if (!repository.existsById(id)) {
      log.error("Notification not found for deletion: {}", id);
      throw new IllegalArgumentException("Notification with ID=[" + id + "] not found");
    }

    repository.deleteById(id);
    log.info("Notification deleted successfully: {}", id);
  }

  @Override
  public Optional<Notification> findById(Long id) {
    log.debug("Finding notification by ID: {}", id);

    var result = repository.findById(id)
      .map(entity -> {
        log.debug("Notification found - Transaction: {}, Status: {}, Attempts: {}",
          entity.getTransactionId(), entity.getStatus(), entity.getAttempts());

        return mapper.toDomain(entity);
      });

    if (result.isEmpty()) {
      log.debug("Notification not found with id: {}", id);
    }

    return result;
  }

  @Override
  public List<Notification> findByTransactionId(String transactionId) {
    log.debug("Finding notifications for transaction: {}", transactionId);

    var entities = repository.findByTransactionIdOrderByCreatedAtDesc(transactionId);

    log.debug("Found {} notifications for transaction {}", entities.size(), transactionId);
    return mapper.toDomainList(entities);
  }

  @Override
  public List<Notification> findPendingNotifications(int limit) {
    log.debug("Finding pending notifications, limit: {}", limit);

    var pageable = PageRequest.of(0, limit);
    var entities = repository.findPendingNotifications(pageable);

    log.info("Found {} pending notifications", entities.size());
    return mapper.toDomainList(entities);
  }
}