package com.example.demo.infra.repository.jpa;

import com.example.demo.infra.repository.jpa.entities.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

  List<NotificationEntity> findByTransactionIdOrderByCreatedAtDesc(String transactionId);

  @Query("""
            SELECT n FROM NotificationEntity n
            WHERE n.status = 'PENDING'
              AND n.attempts < n.maxAttempts
            ORDER BY n.createdAt ASC
           """)
  List<NotificationEntity> findPendingNotifications(Pageable pageable);

  @Query("""
            SELECT n FROM NotificationEntity n
            WHERE n.status = 'FAILED'
              AND n.attempts < :maxAttempts
            ORDER BY n.createdAt ASC
           """)
  List<NotificationEntity> findFailedNotificationsForRetry(
          @Param("maxAttempts") int maxAttempts,
          Pageable pageable
  );
}






