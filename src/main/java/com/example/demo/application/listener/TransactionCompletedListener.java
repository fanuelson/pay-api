package com.example.demo.application.listener;

import com.example.demo.infra.messaging.transaction.TransactionCompletedEvent;

public interface TransactionCompletedListener {
  void handle(TransactionCompletedEvent event);
}
