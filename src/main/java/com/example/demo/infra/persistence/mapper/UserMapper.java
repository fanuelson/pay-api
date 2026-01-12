package com.example.demo.infra.persistence.mapper;

import com.example.demo.domain.model.User;
import com.example.demo.infra.persistence.repository.entities.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toDomain(UserEntity entity);
  UserEntity toEntity(User domain);
  List<User> toDomainList(List<UserEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  UserEntity updateEntity(@MappingTarget UserEntity entity, User domain);
}