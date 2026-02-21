package com.example.demo.infra.repository.wallet.mapper;

import com.example.demo.domain.wallet.Wallet;
import com.example.demo.infra.repository.wallet.jpa.entities.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.function.UnaryOperator;

@Mapper(componentModel = "spring")
public interface WalletMapper {

  @Mapping(source = "id", target = "id.value")
  Wallet toDomain(WalletEntity entity);

  @Mapping(source = "id.value", target = "id")
  WalletEntity toEntity(Wallet domain);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  WalletEntity updateEntity(@MappingTarget WalletEntity entity, Wallet domain);

  default UnaryOperator<WalletEntity> updateFrom(final Wallet domain) {
    return existing -> updateEntity(existing, domain);
  }
}