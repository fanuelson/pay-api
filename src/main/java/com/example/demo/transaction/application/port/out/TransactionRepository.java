package com.example.demo.transaction.application.port.out;

import com.example.demo.shared.domain.TransactionId;
import com.example.demo.transaction.domain.Transaction;

public interface TransactionRepository {
  TransactionId create(Transaction transaction);
}
