package com.example.demo.infra.repository.mapper;


import com.example.demo.domain.model.Wallet;
import com.example.demo.infra.repository.jpa.entities.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.function.UnaryOperator;

@Mapper(componentModel = "spring")
public interface WalletMapper {

  Wallet toDomain(WalletEntity entity);
  WalletEntity toEntity(Wallet domain);
  List<Wallet> toDomainList(List<WalletEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  WalletEntity updateEntity(@MappingTarget WalletEntity entity, Wallet domain);

  default UnaryOperator<WalletEntity> updateFrom(final Wallet domain) {
    return existing -> updateEntity(existing, domain);
  }
}