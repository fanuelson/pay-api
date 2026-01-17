package com.example.demo.application.saga.transfer.step;

import com.example.demo.application.saga.SagaStep;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditStep implements SagaStep<TransferSagaContext> {

  private final WalletRepository walletRepository;

  @Override
  public String getName() {
    return "Credit";
  }

  @Override
  public void execute(TransferSagaContext context) {
    var wallet = context.getPayeeWallet();
    wallet.credit(context.getAmountInCents());
    walletRepository.save(wallet);
  }

  @Override
  public void compensate(TransferSagaContext context, String cause) {
    var wallet = context.getPayeeWallet();
    wallet.debit(context.getAmountInCents());
    walletRepository.save(wallet);
  }
}
