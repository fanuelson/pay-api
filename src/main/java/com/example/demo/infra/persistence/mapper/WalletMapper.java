package com.example.demo.infra.persistence.mapper;


import com.example.demo.domain.model.Wallet;
import com.example.demo.infra.persistence.repository.entities.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {

  Wallet toDomain(WalletEntity entity);
  WalletEntity toEntity(Wallet domain);
  List<Wallet> toDomainList(List<WalletEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  WalletEntity updateEntity(@MappingTarget WalletEntity entity, Wallet domain);
}