package com.example.demo.domain.helper;

import static java.util.Objects.isNull;

public class StringHelper {

  private StringHelper() {
  }

  public static boolean isBlank(final String str) {
    return isNull(str) || str.isBlank();
  }

  public static boolean isNotBlank(final String str) {
    return !isBlank(str);
  }

  public static String joinWith(String joiner, String... strs) {
    return String.join(joiner, strs);
  }

  public static String join(String... strs) {
    return joinWith("", strs);
  }

}
