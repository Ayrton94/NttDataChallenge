package com.bank.accounts.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Movement {
    private Long id;
    private Long accountId;
    private Long customerId;
    private Instant movementDate;
    private MovementType type;          // DEBIT / CREDIT
    private BigDecimal amount;          // > 0
    private BigDecimal balance;         // saldo resultante
    private boolean status;
}