package com.example.demo.domain.model;

public enum TransactionStatus {
    CREATED,      // Transação criada, aguardando processamento
    VALIDATED,
    RESERVED,
    AUTHORIZED,   // Autorizada pelo serviço externo
    COMPLETED,    // Concluída com sucesso
    FAILED,       // Falhou por algum motivo
    REVERSED      // Revertida/Estornada
}
