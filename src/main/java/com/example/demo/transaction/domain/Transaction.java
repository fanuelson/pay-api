package com.example.demo.transaction.domain;

import com.example.demo.shared.domain.TransactionId;
import com.example.demo.shared.domain.UserId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Transaction {
  private final TransactionId id;
  private final UserId payerId;
  private final UserId payeeId;
  private final long amountInCents;

  public static Transaction create(long amountInCents, UserId payerId, UserId payeeId) {
    return new Transaction(
        TransactionId.create(),
        payerId,
        payeeId,
        amountInCents
    );
  }
}
