package com.example.demo.domain.repository;

import com.example.demo.domain.model.Wallet;

import java.util.Optional;

public interface WalletRepository {
  Wallet save(Wallet wallet);
  Wallet update(Long id, Wallet wallet);
  void delete(Long id);
  Optional<Wallet> findById(Long id);
  Optional<Wallet> findByUserId(Long userId);
  boolean existsByUserId(Long userId);
}

