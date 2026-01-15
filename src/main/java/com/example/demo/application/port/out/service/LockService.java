package com.example.demo.application.port.out.service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface LockService {
  <T> T withLock(String key, long waitTime, long leaseTime, TimeUnit unit, Supplier<T> block);
  boolean isLocked(final String key);
}