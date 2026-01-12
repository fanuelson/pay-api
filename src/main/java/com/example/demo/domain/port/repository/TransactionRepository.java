package com.example.demo.domain.port.repository;

import com.example.demo.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

  Transaction save(Transaction transaction);
  Transaction update(String id, Transaction transaction);
  void delete(String id);
  Optional<Transaction> findById(String id);
  List<Transaction> findByUserId(Long userId, int limit);
  List<Transaction> findByUserId(Long userId);

}
