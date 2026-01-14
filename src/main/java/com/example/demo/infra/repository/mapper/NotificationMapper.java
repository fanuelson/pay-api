package com.example.demo.infra.repository.mapper;


import com.example.demo.domain.model.Notification;
import com.example.demo.infra.repository.jpa.entities.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.function.UnaryOperator;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  Notification toDomain(NotificationEntity entity);
  NotificationEntity toEntity(Notification domain);
  List<Notification> toDomainList(List<NotificationEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "transactionId", ignore = true)
  @Mapping(target = "recipientId", ignore = true)
  @Mapping(target = "channel", ignore = true)
  @Mapping(target = "maxAttempts", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  NotificationEntity updateEntity(@MappingTarget NotificationEntity entity, Notification domain);

  default UnaryOperator<NotificationEntity> updateFrom(final Notification domain) {
    return existing -> updateEntity(existing, domain);
  }
}