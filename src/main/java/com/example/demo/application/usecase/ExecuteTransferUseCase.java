package com.example.demo.application.usecase;

import com.example.demo.application.chain.TransferChain;
import com.example.demo.application.chain.transfer.TransferContext;
import com.example.demo.application.port.in.ExecuteTransferCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecuteTransferUseCase {

  private final TransferChain chain;

  public void execute(ExecuteTransferCommand command) {
    var context = TransferContext.builder()
        .transactionId(command.getTransactionId())
        .build();

    chain.execute(context);
  }
}
