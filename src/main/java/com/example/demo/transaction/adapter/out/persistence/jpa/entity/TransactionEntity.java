package com.example.demo.transaction.adapter.out.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

  @Id
  private String id;

  @Column(name = "payer_id")
  private Long payerId;

  @Column(name = "payee_id")
  private Long payeeId;

  @Column(name = "amount_in_cents")
  private long amountInCents;

  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  private LocalDateTime createdAt;
}
