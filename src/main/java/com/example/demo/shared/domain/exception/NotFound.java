package com.example.demo.shared.domain.exception;

import java.util.function.Supplier;

public final class NotFound {

  private static final String MSG_TEMPLATE = "[%s]: [%s] não encontrado";

  private NotFound() {
  }

  public static Supplier<NotFoundException> of(String resource, Object id) {
    return () -> new NotFoundException(MSG_TEMPLATE.formatted(resource, id));
  }
}
