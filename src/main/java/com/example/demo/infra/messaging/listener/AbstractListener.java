package com.example.demo.infra.messaging.listener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractListener {

  protected void logError(String msg, Throwable t) {
    log.error(msg, t);
  }
}
