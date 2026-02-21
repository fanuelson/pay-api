package com.example.demo.infra.repository.notification.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.domain.notification.model.Notification;
import com.example.demo.infra.repository.notification.jpa.entities.NotificationEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface NotificationMapper {


  @Mapping(target = "id.value", source = "id")
  Notification toDomain(NotificationEntity entity);

  @Mapping(source = "id.value", target = "id")
  NotificationEntity toEntity(Notification domain);

  List<Notification> toDomainList(List<NotificationEntity> entities);


}