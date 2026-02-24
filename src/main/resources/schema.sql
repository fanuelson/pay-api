-- Tabela de Usuários
CREATE TABLE users
(
    id                            BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name                     VARCHAR(255) NOT NULL,
    document                      VARCHAR(14)  NOT NULL UNIQUE,
    email                         VARCHAR(255) NOT NULL UNIQUE,
    enabled_notification_channels VARCHAR(255) NOT NULL DEFAULT 'EMAIL',
    user_type                     VARCHAR(20)  NOT NULL,
    created_at                    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_user_type CHECK (user_type IN ('COMMON', 'MERCHANT'))
);

CREATE INDEX idx_user_document ON users (document);
CREATE INDEX idx_user_email ON users (email);


-- Tabela de Carteiras
CREATE TABLE wallets
(
    id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                   BIGINT    NOT NULL UNIQUE,
    balance_in_cents          BIGINT    NOT NULL DEFAULT 0,
    reserved_balance_in_cents BIGINT    NOT NULL DEFAULT 0,
    version                   INT       NOT NULL DEFAULT 0,
    created_at                TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_balance CHECK (balance_in_cents >= 0)
);

CREATE INDEX idx_wallet_user_id ON wallets (user_id);

-- Tabela de Transações
CREATE TABLE transactions
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
    CONSTRAINT chk_different_users CHECK (payer_id != payee_id
) ,
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'AUTHORIZED', 'COMPLETED', 'FAILED', 'REVERSED'))
);

CREATE INDEX idx_transaction_payer ON transactions (payer_id);
CREATE INDEX idx_transaction_payee ON transactions (payee_id);
CREATE INDEX idx_transaction_status ON transactions (status);
CREATE INDEX idx_transaction_created_at ON transactions (created_at);

-- Tabela de Notificações
CREATE TABLE notifications
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(36) NOT NULL,
    recipient_id   BIGINT      NOT NULL,
    channel        VARCHAR(50) NOT NULL,
    status         VARCHAR(20) NOT NULL,
    message        TEXT,
    attempts       INT         NOT NULL DEFAULT 0,
    max_attempts   INT         NOT NULL DEFAULT 3,
    error_message  TEXT,
    created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at        TIMESTAMP,

    CONSTRAINT fk_notification_transaction FOREIGN KEY (transaction_id) REFERENCES transactions (id),
    CONSTRAINT fk_notification_recipient FOREIGN KEY (recipient_id) REFERENCES users (id),
    CONSTRAINT chk_notification_status CHECK (status IN ('PENDING', 'SENT', 'FAILED'))
);

CREATE INDEX idx_notification_transaction ON notifications (transaction_id);
CREATE INDEX idx_notification_recipient ON notifications (recipient_id);
CREATE INDEX idx_notification_status_attempts ON notifications (status, attempts);