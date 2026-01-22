package com.example.demo.domain.helper;

import java.time.LocalDateTime;

public class DateTimeHelper {

  private DateTimeHelper() {
  }

  public static LocalDateTime now() {
    return LocalDateTime.now();
  }
}
