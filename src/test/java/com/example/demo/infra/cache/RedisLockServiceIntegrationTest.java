package com.example.demo.infra.cache;

import com.example.demo.infra.config.RedisContainerConfig;
import com.example.demo.infra.exception.InfraException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
  classes = {
    RedisLockService.class
  },
  webEnvironment = SpringBootTest.WebEnvironment.NONE  // Não sobe servidor web
)
@ActiveProfiles("test")
@Testcontainers
@Import(RedisContainerConfig.class)
class RedisLockServiceIntegrationTest {

  @Autowired
  private RedisLockService lockService;

  @Autowired
  private RedissonClient redissonClient;

  @BeforeEach
  void setUp() {
    // Limpa qualquer lock que possa ter ficado de testes anteriores
    redissonClient.getKeys().flushall();
  }

  @Test
  @DisplayName("should execute block and return result when lock is acquired")
  void shouldExecuteBlockAndReturnResult() {
    String result = lockService.withLock(
      "test-key",
      5, 10, TimeUnit.SECONDS,
      () -> "success"
    );

    assertEquals("success", result);
  }

  @Test
  @DisplayName("should release lock after execution")
  void shouldReleaseLockAfterExecution() {
    lockService.withLock(
      "test-key",
      5, 10, TimeUnit.SECONDS,
      () -> "done"
    );

    assertFalse(lockService.isLocked("test-key"));
  }

  @Test
  @DisplayName("should release lock even when block throws exception")
  void shouldReleaseLockWhenBlockThrowsException() {
    assertThrows(InfraException.class, () ->
      lockService.withLock(
        "test-key",
        5, 10, TimeUnit.SECONDS,
        () -> {
          throw new RuntimeException("Simulated error");
        }
      )
    );

    assertFalse(lockService.isLocked("test-key"));
  }

  @Test
  @DisplayName("should throw InfraException when lock cannot be acquired")
  void shouldThrowWhenLockNotAcquired() throws Exception {
    String lockKey = "contested-key";
    CountDownLatch lockAcquired = new CountDownLatch(1);
    CountDownLatch testComplete = new CountDownLatch(1);

    // Thread 1: acquire lock and hold it
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(() -> {
      lockService.withLock(lockKey, 5, 30, TimeUnit.SECONDS, () -> {
        lockAcquired.countDown();
        try {
          testComplete.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        return null;
      });
    });

    // Wait for thread 1 to acquire the lock
    assertTrue(lockAcquired.await(5, TimeUnit.SECONDS));

    // Thread 2 (main): try to acquire same lock with short timeout
    InfraException exception = assertThrows(InfraException.class, () ->
      lockService.withLock(lockKey, 1, 10, TimeUnit.SECONDS, () -> "should not execute")
    );

    assertTrue(exception.getMessage().contains("Failed to acquire lock"), exception.getMessage());

    testComplete.countDown();
    executor.shutdown();
    executor.awaitTermination(5, TimeUnit.SECONDS);
  }

  @Test
  @DisplayName("should ensure mutual exclusion between concurrent threads")
  void shouldEnsureMutualExclusion() throws Exception {
    String lockKey = "concurrent-key";
    int threadCount = 5;
    AtomicInteger counter = new AtomicInteger(0);
    AtomicBoolean concurrencyViolation = new AtomicBoolean(false);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(threadCount);

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await();
          lockService.withLock(lockKey, 30, 10, TimeUnit.SECONDS, () -> {
            int current = counter.incrementAndGet();
            if (current > 1) {
              concurrencyViolation.set(true);
            }
            try {
              Thread.sleep(50); // Simulate work
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
            counter.decrementAndGet();
            return null;
          });
        } catch (Exception e) {
          // Lock acquisition timeout is expected for some threads
        } finally {
          endLatch.countDown();
        }
      });
    }

    startLatch.countDown();
    assertTrue(endLatch.await(30, TimeUnit.SECONDS));

    assertFalse(concurrencyViolation.get(), "Multiple threads executed critical section simultaneously");

    executor.shutdown();
  }

  @Test
  @DisplayName("should propagate original exception type wrapped in InfraException")
  void shouldPropagateOriginalException() {
    IllegalArgumentException originalException = new IllegalArgumentException("Invalid argument");

    InfraException thrown = assertThrows(InfraException.class, () ->
      lockService.withLock(
        "test-key",
        5, 10, TimeUnit.SECONDS,
        () -> {
          throw originalException;
        }
      )
    );

    assertEquals(originalException, thrown.getCause());
  }

}
