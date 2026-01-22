package com.example.demo.domain.helper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeHelper {

  private DateTimeHelper() {
  }

  public static LocalDateTime now() {
    return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
  }
}
