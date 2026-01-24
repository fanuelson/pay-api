package com.example.demo.domain.repository;

import com.example.demo.domain.model.Wallet;

import java.util.Optional;

public interface WalletRepository {
  Wallet save(Wallet wallet);
  Wallet update(Long id, Wallet wallet);
  Optional<Wallet> findById(Long id);
  Optional<Wallet> findByUserId(Long userId);

  Wallet updateTest(Long payerId, Long id, Wallet wallet);
}

