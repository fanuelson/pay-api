package com.example.demo.application.chain.transfer.step;

import com.example.demo.application.chain.transfer.TransferContext;
import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReserveBalanceStep implements TransferHandler {

  private final WalletRepository walletRepository;

  @Override
  public String name() {
    return "ReserveBalance";
  }

  @Override
  public void execute(TransferContext context) {
    var wallet = context.getPayerWallet();
    wallet.reserve(context.getAmountInCents());
    walletRepository.update(wallet.getId(), wallet);
  }

  @Override
  public void compensate(TransferContext context, Exception cause) {
    var wallet = context.getPayerWallet();
    wallet.releaseReserve(context.getAmountInCents());
    walletRepository.update(wallet.getId(), wallet);
  }
}
