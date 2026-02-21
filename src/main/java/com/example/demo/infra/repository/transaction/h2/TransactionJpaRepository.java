package com.example.demo.infra.repository.transaction.h2;

import com.example.demo.infra.repository.transaction.h2.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {
}
