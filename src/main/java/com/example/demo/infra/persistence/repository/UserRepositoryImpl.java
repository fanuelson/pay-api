package com.example.demo.infra.persistence.repository;

import com.example.demo.domain.model.User;
import com.example.demo.domain.port.repository.UserRepository;
import com.example.demo.infra.persistence.mapper.UserMapper;
import com.example.demo.infra.persistence.repository.jpa.UserJpaRepository;
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
    log.debug("Saving user: {}", user.getEmail());

    var entity = mapper.toEntity(user);
    var savedEntity = repository.save(entity);

    log.info("User saved successfully with ID: {}", savedEntity.getId());
    return mapper.toDomain(savedEntity);
  }

  @Override
  public User update(Long id, User user) {
    log.debug("Updating user with ID: {}", id);

    var existingEntity = repository.findById(id)
      .orElseThrow(() -> {
        log.error("User not found for update: {}", id);
        return new IllegalArgumentException("User id=[" + id + "] not found for update");
      });

    mapper.updateEntity(existingEntity, user);
    var savedEntity = repository.save(existingEntity);

    log.info("User updated successfully: {}", id);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public void delete(Long id) {
    log.debug("Deleting user with ID: {}", id);

    if (!repository.existsById(id)) {
      log.error("User not found for deletion: {}", id);
      throw new IllegalArgumentException("User id=[" + id + "] not found for deletion");
    }

    repository.deleteById(id);
    log.info("User deleted successfully: {}", id);
  }

  @Override
  public Optional<User> findById(Long id) {
    log.debug("Finding user by ID: {}", id);

    var result = repository.findById(id)
      .map(entity -> {
        log.debug("User found: {}", entity.getEmail());
        return mapper.toDomain(entity);
      });

    if (result.isEmpty()) {
      log.debug("User not found with ID: {}", id);
    }

    return result;
  }

  @Override
  public Optional<User> findByDocument(String document) {
    log.debug("Finding user by document: {}***", document.substring(0, 3));

    return repository.findByDocument(document)
      .map(entity -> {
        log.debug("User found with document");
        return mapper.toDomain(entity);
      });
  }

  @Override
  public Optional<User> findByEmail(String email) {
    log.debug("Finding user by email: {}", email);

    return repository.findByEmail(email)
      .map(entity -> {
        log.debug("User found with email");
        return mapper.toDomain(entity);
      });
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
