package com.example.demo.infra.repository;

import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.model.Wallet;
import com.example.demo.domain.port.repository.WalletRepository;
import com.example.demo.infra.repository.jpa.WalletJpaRepository;
import com.example.demo.infra.repository.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.Optional.of;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

  private final WalletJpaRepository repository;
  private final WalletMapper mapper;

  @Override
  public Wallet save(Wallet wallet) {
    return of(wallet)
      .map(mapper::toEntity)
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow();
  }

  @Override
  public Wallet update(Long id, Wallet wallet) {
    return repository.findById(id)
      .map(existing -> mapper.updateEntity(existing, wallet))
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow(() -> {
        log.error("Wallet not found for update: {}", id);
        return new ElementNotFoundException("Wallet with id=[" + id + "] not found");
      });
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public Optional<Wallet> findById(Long id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<Wallet> findByUserId(Long userId) {
    return repository.findByUserId(userId).map(mapper::toDomain);
  }

  @Override
  public boolean existsByUserId(Long userId) {
    return repository.existsByUserId(userId);
  }
}
