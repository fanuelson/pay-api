package com.example.demo.infra.repository.notification;

import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.notification.model.NotificationId;
import com.example.demo.infra.repository.notification.jpa.NotificationJpaRepository;
import com.example.demo.infra.repository.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
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
  public Optional<Notification> findById(NotificationId id) {
    return repository.findById(Long.valueOf(id.value())).map(mapper::toDomain);
  }

}