package com.example.demo.infra.persistence.repository.jpa;

import com.example.demo.domain.model.TransactionStatus;
import com.example.demo.infra.persistence.repository.entities.TransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {

  @Query("""
           SELECT t FROM TransactionEntity t
           WHERE t.payerId = :userId OR t.payeeId = :userId
           ORDER BY t.createdAt DESC
          """)
  List<TransactionEntity> findByUserId(
    @Param("userId") Long userId,
    Pageable pageable
  );

  List<TransactionEntity> findByStatusOrderByCreatedAtDesc(
    TransactionStatus status,
    Pageable pageable
  );
}