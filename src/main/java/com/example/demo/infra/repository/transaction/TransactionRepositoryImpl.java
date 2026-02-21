package com.example.demo.infra.repository.transaction;

import com.example.demo.domain.transaction.model.Transaction;
import com.example.demo.domain.transaction.model.TransactionId;
import com.example.demo.domain.transaction.repository.TransactionRepository;
import com.example.demo.infra.repository.transaction.h2.TransactionJpaRepository;
import com.example.demo.infra.repository.transaction.h2.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

//  private final TransactionMapper mapper;
//  private final TransactionMongoRepository repository;

  private final TransactionMapper mapper;
  private final TransactionJpaRepository repository;

  @Override
  public Transaction save(Transaction transaction) {
    final var document = mapper.toEntity(transaction);
    final var saved = repository.save(document);
    return mapper.toDomain(saved);
  }

  @Override
  public Optional<Transaction> findById(TransactionId id) {
    return repository.findById(id.value()).map(mapper::toDomain);
  }
}
