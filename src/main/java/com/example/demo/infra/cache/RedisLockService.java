package com.example.demo.infra.cache;

import com.example.demo.application.port.out.service.LockService;
import com.example.demo.infra.cache.exception.LockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.example.demo.domain.helper.StringHelper.join;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLockService implements LockService {

  private static final String LOCK_PREFIX = "simplepay:lock:";

  private final RedissonClient redissonClient;

  @Override
  public <T> T withLock(
    String key,
    long waitTime,
    long leaseTime,
    TimeUnit unit,
    Supplier<T> block
  ) {
    logDebug("Executing with lock", key);
    try {
      final RLock lock = getLock(key);
      boolean acquired = lock.tryLock(waitTime, leaseTime, unit);

      if (!acquired) {
        throw LockException.alreadyAcquired(key);
      }

      try {
        return block.get();
      } finally {
        if (lock.isHeldByCurrentThread()) {
          lock.unlock();
          logDebug("Lock released after execution", key);
        } else {
          logDebug("Lock not released after execution, is not held by current thread", key);
        }
      }

    } catch (InterruptedException e) {
      logError("Interrupted while executing", key, e);
      Thread.currentThread().interrupt();
      throw LockException.interrupted(key);
    } catch (Exception e) {
      logError("Error", key, e);
      throw LockException.of("Error: " + e.getMessage(), e);
    }
  }

  public boolean isLocked(final String key) {
    return getLock(key).isLocked();
  }

  private RLock getLock(final String key) {
    return this.redissonClient.getLock(mountKey(key));
  }

  private static String mountKey(final String key) {
    return join(LOCK_PREFIX, key);
  }

  private void logDebug(final String msg, final String key) {
    log.debug("{} - lock key: {}", msg, mountKey(key));
  }

  private void logWarn(final String msg, final String key) {
    log.warn("{} - lock key: {}", msg, mountKey(key));
  }

  private void logError(final String msg, final String key, final Exception ex) {
    log.error("{} - lock key: {}", msg, mountKey(key), ex);
  }
}
