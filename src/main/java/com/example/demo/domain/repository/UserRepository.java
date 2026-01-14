package com.example.demo.domain.repository;

import com.example.demo.domain.model.User;

import java.util.Optional;

public interface UserRepository {
  User save(User user);
  User update(Long id, User user);
  void delete(Long id);
  Optional<User> findById(Long id);
  Optional<User> findByDocument(String document);
  Optional<User> findByEmail(String email);
  boolean existsByDocument(String document);
  boolean existsByEmail(String email);
}