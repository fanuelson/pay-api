package com.example.demo.infra.repository.jpa;

import com.example.demo.infra.repository.jpa.entities.OutboxEventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, Long> {

  @Query("""
      SELECT e FROM OutboxEventEntity e
      WHERE e.status = 'PENDING'
      ORDER BY e.createdAt ASC
      """)
  List<OutboxEventEntity> findPending(Pageable pageable);

  @Query("""
      SELECT e FROM OutboxEventEntity e
      WHERE e.status = 'DISPATCHED'
        AND e.dispatchedAt < :threshold
      ORDER BY e.dispatchedAt ASC
      """)
  List<OutboxEventEntity> findDispatchedBefore(
      @Param("threshold") LocalDateTime threshold,
      Pageable pageable
  );
}
