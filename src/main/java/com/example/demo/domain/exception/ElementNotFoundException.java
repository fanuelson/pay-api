package com.example.demo.domain.exception;

public class ElementNotFoundException extends DomainException {

  public ElementNotFoundException(String message) {
    super(message);
  }

  public static ElementNotFoundException of(String element, Object id) {
    return new ElementNotFoundException("%s not found with id: %s".formatted(element, String.valueOf(id)));
  }
}