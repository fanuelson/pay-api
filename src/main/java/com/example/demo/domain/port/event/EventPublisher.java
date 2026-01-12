package com.example.demo.domain.port.event;

public interface EventPublisher<T> {
  void publish(T event);
}
