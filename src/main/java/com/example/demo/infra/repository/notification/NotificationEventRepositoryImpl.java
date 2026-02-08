package com.example.demo.infra.repository.notification;

import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.domain.notification.model.NotificationId;
import com.example.demo.domain.notification.repository.NotificationEventRepository;
import com.example.demo.infra.repository.notification.jpa.NotificationEventJpaRepository;
import com.example.demo.infra.repository.notification.mapper.NotificationEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationEventRepositoryImpl implements NotificationEventRepository {

  private final NotificationEventJpaRepository repository;
  private final NotificationEventMapper mapper;

  @Override
  public NotificationEvent save(NotificationEvent notification) {
    return Optional.of(notification)
      .map(mapper::toEntity)
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow();
  }

  public Optional<NotificationEvent> findByNotificationId(NotificationId notificationId) {
    return repository.findByNotificationId(Long.valueOf(notificationId.value()))
      .map(mapper::toDomain);
  }

}