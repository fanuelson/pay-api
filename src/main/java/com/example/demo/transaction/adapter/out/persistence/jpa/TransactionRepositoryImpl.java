package com.example.demo.transaction.adapter.out.persistence.jpa;

import com.example.demo.shared.domain.TransactionId;
import com.example.demo.transaction.adapter.out.persistence.jpa.entity.TransactionEntity;
import com.example.demo.transaction.adapter.out.persistence.jpa.mapper.TransactionMapper;
import com.example.demo.transaction.adapter.out.persistence.jpa.repository.TransactionJpaRepository;
import com.example.demo.transaction.application.port.out.TransactionRepository;
import com.example.demo.transaction.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

  private final TransactionJpaRepository repository;
  private final TransactionMapper mapper;

  @Override
  public TransactionId create(Transaction transaction) {
    TransactionEntity saved = repository.saveAndFlush(mapper.toEntity(transaction));
    return new TransactionId(saved.getId());
  }
}
