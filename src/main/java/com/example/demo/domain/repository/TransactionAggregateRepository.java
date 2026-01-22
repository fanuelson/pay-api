package com.example.demo.domain.repository;

import com.example.demo.domain.aggregate.TransactionAggregate;
import com.example.demo.domain.vo.TransactionId;

public interface TransactionAggregateRepository {

  TransactionAggregate findById(TransactionId transactionId);

}
