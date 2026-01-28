package com.example.demo.infra.repository;

import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.exception.InsufficientBalanceException;
import com.example.demo.domain.model.Wallet;
import com.example.demo.domain.repository.WalletRepository;
import com.example.demo.domain.vo.UserId;
import com.example.demo.domain.vo.WalletId;
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
  public Wallet update(WalletId id, Wallet wallet) {
    return repository.findById(id.asLong())
      .map(existing -> mapper.updateEntity(existing, wallet))
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow(() -> {
        log.error("Wallet not found for update: {}", id);
        return new ElementNotFoundException("Wallet with id=[" + id + "] not found");
      });
  }

  @Override
  public void credit(WalletId id, Long amountInCents) {
    int updated = repository.creditBalance(id.asLong(), amountInCents);
    if (updated == 0) {
      throw new ElementNotFoundException("Wallet with id=[" + id + "] not found");
    }
    log.debug("Credited {} cents to wallet {}", amountInCents, id);
  }

  @Override
  public void debit(WalletId id, Long amountInCents) {
    int updated = repository.debitBalance(id.asLong(), amountInCents);
    if (updated == 0) {
      var wallet = repository.findById(id.asLong());
      if (wallet.isEmpty()) {
        throw new ElementNotFoundException("Wallet with id=[" + id + "] not found");
      }
      throw new InsufficientBalanceException("Insufficient balance in wallet id=[" + id + "]");
    }
    log.debug("Debited {} cents from wallet {}", amountInCents, id);
  }

  @Override
  public Optional<Wallet> findById(WalletId id) {
    return repository.findById(id.asLong()).map(mapper::toDomain);
  }

  @Override
  public Optional<Wallet> findByUserId(UserId userId) {
    return repository.findByUserId(userId.asLong()).map(mapper::toDomain);
  }
}
