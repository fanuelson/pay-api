package com.example.demo.domain.model;

public record RetryOptions(int attempts, int maxAttempts, String cause) {

  public RetryOptions(int attempts, int maxAttempts) {
    this(attempts, maxAttempts, null);
  }

  public RetryOptions(int maxAttempts) {
    this(0, maxAttempts);
  }

  public RetryOptions incrementAttempts() {
    return new RetryOptions(this.attempts + 1, maxAttempts, cause);
  }

  public boolean hasReachedMaxAttempts() {
    return this.attempts >= this.maxAttempts;
  }

  public boolean canRetry() {
    return !hasReachedMaxAttempts();
  }
}
