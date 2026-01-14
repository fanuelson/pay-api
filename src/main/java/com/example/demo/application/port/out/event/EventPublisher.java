package com.example.demo.application.port.out.event;

public interface EventPublisher<T> {
  void publish(T event);
}
