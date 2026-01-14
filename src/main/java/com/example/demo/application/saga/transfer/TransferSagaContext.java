package com.example.demo.application.saga.transfer;

import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.User;
import com.example.demo.domain.model.Wallet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferSagaContext {

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
