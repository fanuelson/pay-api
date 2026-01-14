package com.example.demo.infra.repository;

import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.repository.NotificationRepository;
import com.example.demo.infra.repository.mapper.NotificationMapper;
import com.example.demo.infra.repository.jpa.NotificationJpaRepository;
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
    return Optional.of(notification)
      .map(mapper::toEntity)
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow();
  }

  @Override
  public Notification update(Notification notification) {
    return repository.findById(notification.getId())
      .map(mapper.updateFrom(notification))
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow(() -> ElementNotFoundException.of("Notification", notification.getId()));
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public Optional<Notification> findById(Long id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Notification> findByTransactionId(String transactionId) {
    return mapper.toDomainList(repository.findByTransactionIdOrderByCreatedAtDesc(transactionId));
  }

  @Override
  public List<Notification> findPendingNotifications(int limit) {
    return mapper.toDomainList(repository.findPendingNotifications(PageRequest.of(0, limit)));
  }
}