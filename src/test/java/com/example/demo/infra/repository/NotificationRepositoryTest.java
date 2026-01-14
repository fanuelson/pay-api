package com.example.demo.infra.repository;

import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.model.NotificationStatus;
import com.example.demo.domain.model.NotificationChannel;
import com.example.demo.infra.repository.jpa.NotificationJpaRepository;
import com.example.demo.infra.repository.jpa.config.JpaConfig;
import com.example.demo.infra.repository.mapper.NotificationMapperImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
@ActiveProfiles("test")
@Import({NotificationRepositoryImpl.class, NotificationMapperImpl.class, JpaConfig.class})
public class NotificationRepositoryTest {

  @Autowired
  private NotificationJpaRepository jpaRepository;

  @Autowired
  private NotificationRepositoryImpl repository;

  @Test
  void shouldExecuteFindByIdAndResultEmpty() {
    final var value = repository.findById(1L);
    assertTrue(value.isEmpty());
  }

  @Test
  void shouldExecuteFindByTransactionIdAndResultEmpty() {
    final var value = repository.findByTransactionId("any");
    assertTrue(value.isEmpty());
  }

  @Test
  void shouldExecuteFindPendingNotificationsAndResultEmpty() {
    final var value = repository.findPendingNotifications(10);
    assertTrue(value.isEmpty());
  }

  @Test
  void shouldExecuteDeleteWithEmpty() {
    assertEquals(0, jpaRepository.count());
    repository.delete(1L);
    assertEquals(0, jpaRepository.count());
  }

  @Test
  void shouldExecuteUpdateAndThrowError() {
    ElementNotFoundException ex = assertThrows(
      ElementNotFoundException.class,
      () -> repository.update(Notification.builder().id(1L).build())
    );

    assertNotNull(ex);
    assertTrue(ex.getMessage().contains("not found"));
  }

  @Test
  void shouldExecuteSaveAndThrowError() {
    DataIntegrityViolationException ex = assertThrows(
      DataIntegrityViolationException.class,
      () -> repository.save(Notification.builder().build())
    );

    assertNotNull(ex);
    assertTrue(ex.getMessage().contains("NULL not allowed"));
  }

  @Test
  void shouldExecuteSaveOk() {
    //ACT
    final var result = repository.save(createPendingNotification());

    //ASSERT
    assertNotNull(result);

    Assertions.assertThat(result)
      .extracting("id", "createdAt")
      .isNotNull();

    Assertions.assertThat(result)
      .usingRecursiveComparison()
      .ignoringFields("id", "createdAt")
      .isEqualTo(createPendingNotification());
  }

  @Test
  void shouldExecuteUpdateOk() {
    //ARRANGE
    repository.save(createPendingNotification());

    //ACT
    final var result = repository.update(createSentNotification().withId(1L));

    //ASSERT
    assertNotNull(result);

    Assertions.assertThat(result)
      .extracting("id", "createdAt")
      .isNotNull();

    Assertions.assertThat(result)
      .usingRecursiveComparison()
      .ignoringFields("createdAt")
      .isEqualTo(createSentNotification().withId(1L));
  }

  private Notification createNotification(final NotificationStatus status) {
    return Notification.builder()
      .transactionId("t1")
      .recipientId(10L)
      .channel(NotificationChannel.EMAIL)
      .status(status)
      .build();
  }

  private Notification createPendingNotification() {
    return createNotification(NotificationStatus.PENDING);
  }

  private Notification createSentNotification() {
    return createNotification(NotificationStatus.SENT);
  }
}
