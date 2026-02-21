package com.example.demo.infra.repository.wallet;

import com.example.demo.domain.user.model.UserId;
import com.example.demo.domain.wallet.Wallet;
import com.example.demo.domain.wallet.WalletRepository;
import com.example.demo.infra.repository.wallet.jpa.WalletJpaRepository;
import com.example.demo.infra.repository.wallet.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

  private final WalletMapper mapper;
  private final WalletJpaRepository repository;

  @Override
  public Optional<Wallet> findByUserId(UserId userId) {
    return repository.findByUserId(userId.asLong()).map(mapper::toDomain);
  }

  @Override
  public Wallet save(Wallet wallet) {
    return repository.findById(wallet.getId().asLong())
      .map(mapper.updateFrom(wallet))
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow();
  }
}
