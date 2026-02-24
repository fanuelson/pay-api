package com.example.demo.application.chain.transfer;

public interface TransferHandler {

  String name();

  void execute(TransferContext context);

  default void compensate(TransferContext context, Exception cause) {
  }
}
