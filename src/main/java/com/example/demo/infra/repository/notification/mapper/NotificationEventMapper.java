package com.example.demo.infra.repository.notification.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.infra.repository.notification.jpa.entities.NotificationEventEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface NotificationEventMapper {

  @Mapping(target = "id.value", source = "id")
  @Mapping(target = "notificationId.value", source = "notificationId")
  NotificationEvent toDomain(NotificationEventEntity entity);

  @Mapping(source = "id.value", target = "id")
  @Mapping(source = "notificationId.value", target = "notificationId")
  NotificationEventEntity toEntity(NotificationEvent domain);

  List<NotificationEvent> toDomainList(List<NotificationEventEntity> entities);

}