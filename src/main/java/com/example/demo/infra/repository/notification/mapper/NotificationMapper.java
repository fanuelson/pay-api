package com.example.demo.infra.repository.notification.mapper;


import com.example.demo.domain.notification.model.Notification;
import com.example.demo.infra.repository.notification.jpa.entities.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {


  @Mapping(target = "id.value", source = "id")
  Notification toDomain(NotificationEntity entity);

  @Mapping(source = "id.value", target = "id")
  NotificationEntity toEntity(Notification domain);

  List<Notification> toDomainList(List<NotificationEntity> entities);


}