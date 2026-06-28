package com.example.demo.transaction.adapter.out.persistence.jpa.repository;


import com.example.demo.transaction.adapter.out.persistence.jpa.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {
}
