package com.example.demo.infra.cache.exception;

import com.example.demo.infra.exception.InfraException;
public class LockException extends InfraException {

  private LockException(String msg, Throwable t) {
    super(msg, t);
  }

  public static LockException of(final String msg, final Throwable t) {
    return new LockException(msg, t);
  }

  public static LockException of(final String msg) {
    return new LockException(msg, null);
  }

  public static LockException alreadyAcquired(final String key) {
    return of("Lock key [%s] already acquired".formatted(key), null);
  }

  public static LockException interrupted(final String key) {
    return of("Lock interrupted while executing, key: " + key);
  }
}
