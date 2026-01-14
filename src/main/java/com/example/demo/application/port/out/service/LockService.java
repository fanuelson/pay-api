package com.example.demo.application.port.out.service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface LockService {
  boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit);
  boolean unlock(String key);
  boolean isLocked(String key);
  <T> T withLock(String key, long waitTime, long leaseTime, TimeUnit unit,Supplier<T> block);
}