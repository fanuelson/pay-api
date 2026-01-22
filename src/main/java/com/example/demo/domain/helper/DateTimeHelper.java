package com.example.demo.domain.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeHelper {

  public static LocalDateTime now() {
    return LocalDateTime.now();
  }
}
