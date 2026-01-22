package com.example.demo.application.port.out.event;

public interface Event<T> {
  String channel();
  String key();
  T payload();
}
