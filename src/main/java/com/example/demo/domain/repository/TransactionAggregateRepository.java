package com.example.demo.domain.repository;

import com.example.demo.domain.model.TransactionAggregate;

public interface TransactionAggregateRepository {
  TransactionAggregate findById(String transactionId);
}
