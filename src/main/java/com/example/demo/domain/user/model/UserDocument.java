package com.example.demo.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class UserDocument {

  private final String value;

  public static UserDocument of(String value) {
    return new UserDocument(value);
  }

}
