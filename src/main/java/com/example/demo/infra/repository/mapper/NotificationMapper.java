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

  @Mapping(target = "retry.maxAttempts", source = "maxAttempts")
  @Mapping(target = "retry.attempts", source = "attempts")
  @Mapping(target = "transactionId.value", source = "transactionId")
  @Mapping(target = "id.value", source = "id")
  Notification toDomain(NotificationEntity entity);

  @Mapping(source = "retry.maxAttempts", target = "maxAttempts")
  @Mapping(source = "retry.attempts", target = "attempts")
  @Mapping(source = "transactionId.value", target = "transactionId")
  @Mapping(source = "id.value", target = "id")
  NotificationEntity toEntity(Notification domain);

  List<Notification> toDomainList(List<NotificationEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "transactionId", ignore = true)
  @Mapping(target = "recipientId", ignore = true)
  @Mapping(target = "channel", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(source = "retry.maxAttempts", target = "maxAttempts", ignore = true)
  @Mapping(source = "retry.attempts", target = "attempts")
  NotificationEntity updateEntity(@MappingTarget NotificationEntity entity, Notification domain);

  default UnaryOperator<NotificationEntity> updateFrom(final Notification domain) {
    return entity -> updateEntity(entity, domain);
  }

}