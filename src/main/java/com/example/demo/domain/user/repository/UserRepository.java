package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.model.UserId;

import java.util.Optional;

public interface UserRepository {
  User save(User user);
  User update(Long id, User user);
  void delete(Long id);
  Optional<User> findById(UserId id);
  Optional<User> findByDocument(String document);
  Optional<User> findByEmail(String email);
  boolean existsByDocument(String document);
  boolean existsByEmail(String email);
}