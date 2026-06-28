package com.example.demo.transaction.application.port.in;

import com.example.demo.shared.domain.TransactionId;
import com.example.demo.transaction.application.port.in.command.CreateTransactionCommand;

public interface CreateTransactionUseCase {
  TransactionId execute(CreateTransactionCommand command);
}
