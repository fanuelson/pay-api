package com.example.demo.application.helper;

public class StringHelper {

  private StringHelper() {
  }

  public static boolean isNull(String str) {
    return str == null;
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

  public static String prependIfMissing(String str, String prefix) {
    if (str == null) return null;
    return isBlank(prefix) || str.startsWith(prefix) ? str : prefix + str;
  }
}
