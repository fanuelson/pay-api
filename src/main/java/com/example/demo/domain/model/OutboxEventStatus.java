package com.example.demo.domain.model;

public enum OutboxEventStatus {
  PENDING,
  DISPATCHED,
  FAILED
}
