package com.example.demo.infra.repository.transaction.h2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

  @Id
  private String id;

  @Column(name = "payer_id", nullable = false)
  private String payerId;

  @Column(name = "payee_id", nullable = false)
  private String payeeId;

  @Column(name = "amount_in_cents", nullable = false)
  private Long amountInCents;

}
