package com.example.demo.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionAggregate {

  private String transactionId;
  private Long payerId;
  private Long payeeId;
  private Long amountInCents;
  private Transaction transaction;
  private User payer;
  private User payee;
  private Wallet payerWallet;
  private Wallet payeeWallet;

}
