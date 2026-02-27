CREATE TABLE IF NOT EXISTS accounts (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        customer_id BIGINT NOT NULL,
                                        account_number VARCHAR(30) NOT NULL,
    type VARCHAR(20) NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    status BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_account_number (account_number)
    );

CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);