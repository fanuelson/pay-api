package com.example.demo.application.chain.transfer.step;

import com.example.demo.application.chain.transfer.TransferContext;
import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompleteStep implements TransferHandler {

  private final TransactionRepository transactionRepository;

  @Override
  public String name() {
    return "Complete";
  }

  @Override
  public void execute(TransferContext context) {
    var transaction = context.getTransaction();
    transaction.completed();
    transactionRepository.save(transaction);
  }
}
