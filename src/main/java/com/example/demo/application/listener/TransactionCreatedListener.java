package com.example.demo.application.listener;

import com.example.demo.infra.messaging.transaction.TransactionCreatedEvent;

public interface TransactionCreatedListener {
  void handle(TransactionCreatedEvent event);
}
