package com.example.demo.application.saga.transfer.step;

import com.example.demo.application.saga.SagaStep;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReserveBalanceStep implements SagaStep<TransferSagaContext> {

  private final WalletRepository walletRepository;

  @Override
  public String getName() {
    return "ReserveBalance";
  }

  @Override
  public void execute(TransferSagaContext context) {
    var wallet = context.getPayerWallet();
    wallet.debit(context.getAmountInCents());
    walletRepository.save(wallet);
  }

  @Override
  public void compensate(TransferSagaContext context, Exception cause) {
    var wallet = context.getPayerWallet();
    wallet.credit(context.getAmountInCents());
    walletRepository.save(wallet);
  }
}
