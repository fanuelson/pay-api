package com.example.demo.domain.repository;

import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.vo.TransactionId;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

  Transaction save(Transaction transaction);
  Transaction update(TransactionId id, Transaction transaction);
  void delete(String id);
  Optional<Transaction> findById(TransactionId id);
  List<Transaction> findByUserId(Long userId, int limit);

}
