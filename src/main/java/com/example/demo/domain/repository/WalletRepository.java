package com.example.demo.domain.repository;

import com.example.demo.domain.model.Wallet;
import com.example.demo.domain.vo.WalletId;

import java.util.Optional;

public interface WalletRepository {
  Wallet save(Wallet wallet);
  Wallet update(WalletId id, Wallet wallet);
  Optional<Wallet> findById(WalletId id);
  Optional<Wallet> findByUserId(Long userId);

  void credit(WalletId id, Long amountInCents);
  void debit(WalletId id, Long amountInCents);
}
