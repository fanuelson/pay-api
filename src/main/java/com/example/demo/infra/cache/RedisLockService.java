package com.example.demo.infra.cache;

import com.example.demo.domain.exception.AppException;
import com.example.demo.domain.port.service.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLockService implements LockService {

  private static final String LOCK_PREFIX = "simplepay:lock:";

  private final RedissonClient redissonClient;

  public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) {
    String lockKey = LOCK_PREFIX + key;
    RLock lock = redissonClient.getLock(lockKey);

    log.debug("Trying to acquire lock: {} (wait: {}, lease: {} {})",
            lockKey, waitTime, leaseTime, unit.name());

    try {
      boolean acquired = lock.tryLock(waitTime, leaseTime, unit);

      if (acquired) {
        log.debug("Lock acquired successfully: {}", lockKey);
      } else {
        log.warn("Failed to acquire lock: {} (timeout)", lockKey);
      }

      return acquired;

    } catch (InterruptedException e) {
      log.error("Interrupted while trying to acquire lock: {}", lockKey, e);
      Thread.currentThread().interrupt();
      return false;
    } catch (Exception e) {
      log.error("Error trying to acquire lock: {}", lockKey, e);
      return false;
    }
  }

  public void unlock(String key) {
    String lockKey = LOCK_PREFIX + key;
    RLock lock = redissonClient.getLock(lockKey);

    try {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
        log.debug("Lock released: {}", lockKey);
      } else {
        log.warn("Attempted to unlock a lock not held by current thread: {}", lockKey);
      }
    } catch (Exception e) {
      log.error("Error releasing lock: {}", lockKey, e);
    }
  }

  public boolean isLocked(String key) {
    String lockKey = LOCK_PREFIX + key;
    return redissonClient.getLock(lockKey).isLocked();
  }

  public <T> T withLock(
          String key,
          long waitTime,
          long leaseTime,
          TimeUnit unit,
          Supplier<T> block
  ) {
    String lockKey = LOCK_PREFIX + key;
    RLock lock = redissonClient.getLock(lockKey);

    log.debug("Executing with lock: {}", lockKey);

    try {
      boolean acquired = lock.tryLock(waitTime, leaseTime, unit);

      if (!acquired) {
        throw new AppException();
      }

      try {
        return block.get();
      } finally {
        if (lock.isHeldByCurrentThread()) {
          lock.unlock();
          log.debug("Lock released after execution: {}", lockKey);
        }
      }

    } catch (InterruptedException e) {
      log.error("Interrupted while executing with lock: {}", lockKey, e);
      Thread.currentThread().interrupt();
      throw new AppException(key);
    }
  }
}
