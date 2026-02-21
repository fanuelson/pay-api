package com.example.demo.domain.transaction.repository;

import com.example.demo.domain.transaction.model.Transaction;
import com.example.demo.domain.transaction.model.TransactionId;

import java.util.Optional;

public interface TransactionRepository {

  Transaction save(Transaction transaction);

  Optional<Transaction> findById(TransactionId id);
}
