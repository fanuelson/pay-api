package com.example.demo.wallet.adapter.out.persistence.jpa;

import com.example.demo.shared.domain.UserId;
import com.example.demo.shared.domain.exception.NotFound;
import com.example.demo.wallet.adapter.out.persistence.jpa.entity.WalletEntity;
import com.example.demo.wallet.adapter.out.persistence.jpa.repository.WalletJpaRepository;
import com.example.demo.wallet.application.port.out.WalletRepository;
import com.example.demo.wallet.domain.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

  private final WalletJpaRepository repository;

  @Override
  public Optional<Wallet> findByUserId(UserId userId) {
    return repository.findByUserId(userId.value()).map(this::toDomain);
  }

  @Override
  public void update(Wallet wallet) {
    final var entity = repository.findByUserId(wallet.getUserId().value())
        .orElseThrow(NotFound.of("Wallet", wallet.getUserId().value()));
    entity.setBalanceInCents(wallet.getBalanceInCents());
    repository.save(entity);
  }

  private Wallet toDomain(WalletEntity entity) {
    return new Wallet(UserId.from(entity.getUserId()), entity.getBalanceInCents());
  }
}
