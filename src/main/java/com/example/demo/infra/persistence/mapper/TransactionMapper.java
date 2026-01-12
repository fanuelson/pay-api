package com.example.demo.infra.persistence.mapper;

import com.example.demo.domain.model.Transaction;
import com.example.demo.infra.persistence.repository.entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  Transaction toDomain(TransactionEntity entity);
  TransactionEntity toEntity(Transaction domain);
  List<Transaction> toDomainList(List<TransactionEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "payerId", ignore = true)
  @Mapping(target = "payeeId", ignore = true)
  @Mapping(target = "amountInCents", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  TransactionEntity updateEntity(@MappingTarget TransactionEntity entity, Transaction domain);
}