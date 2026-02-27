CREATE TABLE IF NOT EXISTS movements (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         account_id BIGINT NOT NULL,
                                         customer_id BIGINT NOT NULL,
                                         movement_date TIMESTAMP NOT NULL,
                                         type VARCHAR(20) NOT NULL,       -- DEBIT / CREDIT
    amount DECIMAL(19,2) NOT NULL,   -- valor del movimiento
    balance DECIMAL(19,2) NOT NULL,  -- saldo resultante luego del movimiento
    status TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_mov_account_date (account_id, movement_date),
    INDEX idx_mov_customer_date (customer_id, movement_date)
    );