package com.bank.accounts.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovementResponse {
    private Long id;
    private Long accountId;
    private Long customerId;
    private Instant movementDate;
    private String type;
    private BigDecimal amount;
    private BigDecimal balance;
    private boolean status;
}