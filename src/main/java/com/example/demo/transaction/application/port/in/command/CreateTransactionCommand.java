package com.example.demo.transaction.application.port.in.command;

import com.example.demo.shared.domain.UserId;

public record CreateTransactionCommand(
    long amountInCents,
    UserId payerId,
    UserId payeeId
) {
}
