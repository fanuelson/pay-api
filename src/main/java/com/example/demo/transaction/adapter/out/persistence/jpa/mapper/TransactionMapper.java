package com.example.demo.transaction.adapter.out.persistence.jpa.mapper;

import com.example.demo.shared.domain.TransactionId;
import com.example.demo.shared.domain.UserId;
import com.example.demo.transaction.adapter.out.persistence.jpa.entity.TransactionEntity;
import com.example.demo.transaction.domain.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  @Mapping(target = "createdAt", ignore = true)
  TransactionEntity toEntity(Transaction transaction);

  Transaction toDomain(TransactionEntity entity);

  default String map(TransactionId id) {
    return id == null ? null : id.value();
  }

  default TransactionId mapTransactionId(String value) {
    return value == null ? null : new TransactionId(value);
  }

  default Long map(UserId userId) {
    return userId == null ? null : userId.value();
  }

  default UserId mapUserId(Long value) {
    return value == null ? null : UserId.from(value);
  }
}
