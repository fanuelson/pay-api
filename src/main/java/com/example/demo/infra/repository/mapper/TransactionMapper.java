package com.example.demo.infra.repository.mapper;

import com.example.demo.domain.model.Transaction;
import com.example.demo.infra.repository.jpa.entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.function.UnaryOperator;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  @Mapping(source = "id", target = "id.value")
  @Mapping(source = "payerId", target = "payerId.value")
  @Mapping(source = "payeeId", target = "payeeId.value")
  Transaction toDomain(TransactionEntity entity);

  @Mapping(source = "id.value", target = "id")
  @Mapping(target = "payerId", source = "payerId.value")
  @Mapping(target = "payeeId", source = "payeeId.value")
  TransactionEntity toEntity(Transaction domain);

  List<Transaction> toDomainList(List<TransactionEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "payerId", ignore = true)
  @Mapping(target = "payeeId", ignore = true)
  @Mapping(target = "amountInCents", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  TransactionEntity updateEntity(@MappingTarget TransactionEntity entity, Transaction domain);

  default UnaryOperator<TransactionEntity> updateFrom(final Transaction domain) {
    return existing -> updateEntity(existing, domain);
  }
}