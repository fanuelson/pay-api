-- ==================================================
-- USUÁRIOS
-- ==================================================

-- Usuário COMMON com saldo suficiente
INSERT INTO users (id, full_name, document, email, user_type, created_at, updated_at)
VALUES (1, 'João Silva', '12345678901', 'joao.silva@email.com', 'COMMON', NOW(), NOW());

-- Usuário COMMON com saldo insuficiente
INSERT INTO users (id, full_name, document, email, user_type, created_at, updated_at)
VALUES (2, 'Maria Santos', '98765432109', 'maria.santos@email.com', 'COMMON', NOW(), NOW());

-- Usuário MERCHANT (não pode enviar transferências)
INSERT INTO users (id, full_name, document, email, user_type, created_at, updated_at)
VALUES (3, 'Loja Tech LTDA', '12345678000190', 'contato@lojatech.com', 'MERCHANT', NOW(), NOW());

-- Usuário COMMON destino 1
INSERT INTO users (id, full_name, document, email, user_type, created_at, updated_at)
VALUES (4, 'Pedro Oliveira', '11122233344', 'pedro.oliveira@email.com', 'COMMON', NOW(), NOW());

-- Usuário COMMON destino 2
INSERT INTO users (id, full_name, document, email, user_type, enabled_notification_channels, created_at, updated_at)
VALUES (5, 'Ana Costa', '55566677788', 'ana.costa@email.com', 'COMMON', 'EMAIL;SMS;FACEBOOK', NOW(), NOW());

-- ==================================================
-- CARTEIRAS
-- ==================================================

-- Carteira do João (R$ 1.000,00)
INSERT INTO wallets (id, user_id, balance_in_cents, created_at, updated_at)
VALUES (1, 1, 100000, NOW(), NOW());

-- Carteira da Maria (R$ 10,00 - saldo insuficiente para transferir R$ 100,00)
INSERT INTO wallets (id, user_id, balance_in_cents, created_at, updated_at)
VALUES (2, 2, 1000, NOW(), NOW());

-- Carteira da Loja (R$ 5.000,00)
INSERT INTO wallets (id, user_id, balance_in_cents, created_at, updated_at)
VALUES (3, 3, 500000, NOW(), NOW());

-- Carteira do Pedro (R$ 500,00)
INSERT INTO wallets (id, user_id, balance_in_cents, created_at, updated_at)
VALUES (4, 4, 50000, NOW(), NOW());

-- Carteira da Ana (R$ 200,00)
INSERT INTO wallets (id, user_id, balance_in_cents, created_at, updated_at)
VALUES (5, 5, 20000, NOW(), NOW());

-- ==================================================
-- SEQUÊNCIAS (para H2)
-- ==================================================

ALTER TABLE users ALTER COLUMN id RESTART WITH 6;
ALTER TABLE wallets ALTER COLUMN id RESTART WITH 6;