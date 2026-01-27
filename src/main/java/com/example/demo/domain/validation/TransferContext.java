package com.example.demo.domain.validation;

import com.example.demo.domain.model.User;
import com.example.demo.domain.model.Wallet;
import com.example.demo.domain.vo.UserId;
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
  private User payerUser;
  private User payeeUser;
  private Wallet payerWallet;
  private Wallet payeeWallet;

  public boolean hasPayerData() {
    return payerUser != null && payerWallet != null;
  }

  public boolean hasPayeeData() {
    return payeeUser != null && payeeWallet != null;
  }

  public boolean hasAllData() {
    return hasPayerData() && hasPayeeData();
  }
}