package com.example.demo.wallet.application.port.out;

import com.example.demo.shared.domain.UserId;
import com.example.demo.wallet.domain.Wallet;

import java.util.Optional;

public interface WalletRepository {
  Optional<Wallet> findByUserId(UserId userId);
  void update(Wallet wallet);
}
