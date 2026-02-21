package com.example.demo.infra.repository.transaction.mapper;

import com.example.demo.domain.transaction.model.Transaction;
import com.example.demo.infra.repository.transaction.mongo.TransactionDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//@Mapper(componentModel = "spring")
public interface TransactionMapper {

//  @Mapping(source = "id", target = "id.value")
//  @Mapping(source = "payerId", target = "payerId.value")
//  @Mapping(source = "payeeId", target = "payeeId.value")
//  Transaction toDomain(TransactionDocument entity);
//
//  @Mapping(source = "id.value", target = "id")
//  @Mapping(source = "payerId.value", target = "payerId")
//  @Mapping(source = "payeeId.value", target = "payeeId")
//  TransactionDocument toDocument(Transaction domain);
}
