package com.example.demo.application.listener;

import com.example.demo.infra.messaging.transaction.TransactionFailedEvent;

public interface TransactionFailedListener {
  void handle(TransactionFailedEvent event);
}
