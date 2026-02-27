package com.example.demo.application.usecase;

import com.example.demo.application.chain.TransferChain;
import com.example.demo.application.port.in.ExecuteTransferCommand;
import com.example.demo.domain.repository.TransactionAggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecuteTransferUseCase {

  private final TransferChain chain;
  private final TransactionAggregateRepository repository;

  public void execute(ExecuteTransferCommand command) {
    var context = repository.findById(command.getTransactionId());
    chain.execute(context);
  }
}
