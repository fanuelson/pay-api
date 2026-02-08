package com.example.demo.domain.transaction.validation;

import com.example.demo.domain.user.model.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferContext {

  private UserId payerId;
  private UserId payeeId;
  private Long amountInCents;

}