package com.example.demo.infra.repository;

import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.vo.TransactionId;
import com.example.demo.infra.repository.jpa.TransactionJpaRepository;
import com.example.demo.infra.repository.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

  private final TransactionJpaRepository repository;
  private final TransactionMapper mapper;

  @Override
  public Transaction save(Transaction transaction) {
    return of(transaction)
      .map(mapper::toEntity)
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow();
  }

  @Override
  public Transaction update(String id, Transaction transaction) {
    return repository.findById(id)
      .map(mapper.updateFrom(transaction))
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow(() -> {
        log.error("Transaction not found for update: {}", id);
        return new ElementNotFoundException("Transaction with id=[" + id + "] not found");
      });
  }

  @Override
  public void delete(String id) {
    repository.deleteById(id);
  }

  @Override
  public Optional<Transaction> findById(TransactionId id) {
    return repository.findById(id.value()).map(mapper::toDomain);
  }

  @Override
  public List<Transaction> findByUserId(Long userId, int limit) {
    return mapper.toDomainList(repository.findByUserId(userId, PageRequest.of(0, limit)));
  }

}
