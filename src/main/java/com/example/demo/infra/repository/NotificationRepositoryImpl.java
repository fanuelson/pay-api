package com.example.demo.infra.repository;

import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.repository.NotificationRepository;
import com.example.demo.domain.vo.NotificationId;
import com.example.demo.domain.vo.TransactionId;
import com.example.demo.infra.repository.jpa.NotificationJpaRepository;
import com.example.demo.infra.repository.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

  private final NotificationJpaRepository repository;
  private final NotificationMapper mapper;

  @Override
  public Notification save(Notification notification) {
    return Optional.of(notification)
      .map(mapper::toEntity)
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow();
  }

  @Override
  public Notification update(Notification notification) {
    return repository.findById(Long.valueOf(notification.getId().value()))
      .map(mapper.updateFrom(notification))
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow(() -> ElementNotFoundException.of("Notification", notification.getId()));
  }

  @Override
  public void delete(NotificationId id) {
    repository.deleteById(Long.valueOf(id.value()));
  }

  @Override
  public Optional<Notification> findById(NotificationId id) {
    return repository.findById(Long.valueOf(id.value())).map(mapper::toDomain);
  }

  @Override
  public List<Notification> findByTransactionId(TransactionId transactionId) {
    return mapper.toDomainList(repository.findByTransactionIdOrderByCreatedAtDesc(transactionId.value()));
  }

  @Override
  public List<Notification> findPendingNotifications(int limit) {
    return mapper.toDomainList(repository.findPendingNotifications(PageRequest.of(0, limit)));
  }
}