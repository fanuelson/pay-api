package com.example.demo.infra.persistence.mapper;


import com.example.demo.domain.model.Notification;
import com.example.demo.infra.persistence.repository.entities.NotificationEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  Notification toDomain(NotificationEntity entity);
  NotificationEntity toEntity(Notification domain);
  List<Notification> toDomainList(List<NotificationEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "transactionId", ignore = true)
  @Mapping(target = "recipientId", ignore = true)
  @Mapping(target = "maxAttempts", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  NotificationEntity updateEntity(@MappingTarget NotificationEntity entity, Notification domain);
}