package com.example.demo.infra.repository;

import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.model.User;
import com.example.demo.domain.port.repository.UserRepository;
import com.example.demo.infra.repository.mapper.UserMapper;
import com.example.demo.infra.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository repository;
  private final UserMapper mapper;

  @Override
  public User save(User user) {
    return Optional.of(user)
      .map(mapper::toEntity)
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow();
  }

  @Override
  public User update(Long id, User user) {
    return repository.findById(id)
      .map(mapper.updateFrom(user))
      .map(repository::save)
      .map(mapper::toDomain)
      .orElseThrow(() -> {
        log.error("User not found for update: {}", id);
        return new ElementNotFoundException("User with id=[" + id + "] not found");
      });
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public Optional<User> findById(Long id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<User> findByDocument(String document) {
    return repository.findByDocument(document).map(mapper::toDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return repository.findByEmail(email).map(mapper::toDomain);
  }

  @Override
  public boolean existsByDocument(String document) {
    return findByDocument(document).isPresent();
  }

  @Override
  public boolean existsByEmail(String email) {
    return findByEmail(email).isPresent();
  }
}
