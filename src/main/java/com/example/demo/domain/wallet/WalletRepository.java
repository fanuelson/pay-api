package com.example.demo.domain.wallet;

import com.example.demo.domain.user.model.UserId;
import java.util.Optional;

public interface WalletRepository {
  Optional<Wallet> findByUserId(UserId id);
  Wallet save(Wallet wallet);
}
