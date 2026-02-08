package com.example.demo.infra.repository.user.mapper;

import com.example.demo.domain.user.model.User;
import com.example.demo.infra.repository.user.jpa.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.function.UnaryOperator;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "id", target = "id.value")
  User toDomain(UserEntity entity);

  @Mapping(source = "id.value", target = "id")
  UserEntity toEntity(User domain);

  List<User> toDomainList(List<UserEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  UserEntity updateEntity(@MappingTarget UserEntity entity, User domain);

  default UnaryOperator<UserEntity> updateFrom(final User domain) {
    return existing -> updateEntity(existing, domain);
  }
}