package com.example.demo.application.event;

public interface EventPublisher<T> {
  void publish(T event);
}
