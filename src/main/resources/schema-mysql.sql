-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS users
(
    id                            BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name                     VARCHAR(255) NOT NULL,
    document                      VARCHAR(14)  NOT NULL UNIQUE,
    email                         VARCHAR(255) NOT NULL UNIQUE,
    enabled_notification_channels VARCHAR(255) NOT NULL DEFAULT 'EMAIL',
    user_type                     VARCHAR(20)  NOT NULL,
    created_at                    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_user_type CHECK (user_type IN ('COMMON', 'MERCHANT')),

    INDEX idx_user_document (document),
    INDEX idx_user_email (email)
);


-- Tabela de Carteiras
CREATE TABLE IF NOT EXISTS wallets
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT    NOT NULL UNIQUE,
    balance_in_cents BIGINT    NOT NULL DEFAULT 0,
    version          INT       NOT NULL DEFAULT 0,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_balance CHECK (balance_in_cents >= 0),

    INDEX idx_wallet_user_id (user_id)
);


-- Tabela de Transações
CREATE TABLE IF NOT EXISTS transactions
(
    id                 VARCHAR(36) PRIMARY KEY,
    payer_id           BIGINT      NOT NULL,
    payee_id           BIGINT      NOT NULL,
    amount_in_cents    BIGINT      NOT NULL,
    status             VARCHAR(20) NOT NULL,
    authorization_code VARCHAR(255),
    error_message      TEXT,
    created_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at       TIMESTAMP,

    CONSTRAINT fk_transaction_payer FOREIGN KEY (payer_id) REFERENCES users (id),
    CONSTRAINT fk_transaction_payee FOREIGN KEY (payee_id) REFERENCES users (id),
    CONSTRAINT chk_amount CHECK (amount_in_cents > 0),
    CONSTRAINT chk_different_users CHECK (payer_id != payee_id),
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'AUTHORIZED', 'COMPLETED', 'FAILED', 'REVERSED')),

    INDEX idx_transaction_payer (payer_id),
    INDEX idx_transaction_payee (payee_id),
    INDEX idx_transaction_status (status),
    INDEX idx_transaction_created_at (created_at)
);


-- Tabela de Notificações
CREATE TABLE IF NOT EXISTS notifications
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id    VARCHAR(36)  NOT NULL,
    recipient_id      BIGINT       NOT NULL,
    recipient_address VARCHAR(255) NOT NULL,
    channel           VARCHAR(20)  NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    message           TEXT,
    attempts          INT          NOT NULL DEFAULT 0,
    max_attempts      INT          NOT NULL DEFAULT 3,
    error_message     TEXT,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    sent_at           TIMESTAMP,

    CONSTRAINT fk_notification_transaction FOREIGN KEY (transaction_id) REFERENCES transactions (id),
    CONSTRAINT fk_notification_recipient FOREIGN KEY (recipient_id) REFERENCES users (id),

    INDEX idx_notification_transaction (transaction_id),
    INDEX idx_notification_recipient (recipient_id),
    INDEX idx_notification_status_attempts (status, attempts)
);