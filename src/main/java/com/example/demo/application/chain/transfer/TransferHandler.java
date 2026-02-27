package com.example.demo.application.chain.transfer;

import com.example.demo.domain.model.TransactionAggregate;
public interface TransferHandler {

  String name();

  void execute(TransactionAggregate context);

  default void compensate(TransactionAggregate context, Exception cause) {
  }
}
