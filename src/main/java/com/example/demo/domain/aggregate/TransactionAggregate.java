package com.example.demo.domain.aggregate;

import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.User;
import com.example.demo.domain.model.Wallet;
import com.example.demo.domain.vo.TransactionId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionAggregate {

  private TransactionId transactionId;
  private Transaction transaction;
  private User payer;
  private User payee;
  private Wallet payerWallet;
  private Wallet payeeWallet;

}
