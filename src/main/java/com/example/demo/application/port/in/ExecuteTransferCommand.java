package com.example.demo.application.port.in;

import com.example.demo.domain.vo.TransactionId;

public record ExecuteTransferCommand(TransactionId transactionId) {
}
