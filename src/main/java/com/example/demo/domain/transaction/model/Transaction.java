package com.example.demo.domain.transaction.model;

import com.example.demo.domain.user.model.UserId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@Getter
@With
@Builder
@AllArgsConstructor
public class Transaction {
  private TransactionId id;
  private UserId payerId;
  private UserId payeeId;
  private Long amountInCents;
}
