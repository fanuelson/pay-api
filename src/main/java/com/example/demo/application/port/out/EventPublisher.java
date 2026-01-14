package com.example.demo.application.port.out;

public interface EventPublisher<T> {
  void publish(T event);
}
