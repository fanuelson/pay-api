package com.example.demo.application.saga;

public interface SagaStep<C> {
  String getName();
  void execute(C context);
  void compensate(C context, Exception cause);
}
