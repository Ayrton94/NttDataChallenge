package com.bank.accounts.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovementReportRow {
    private Instant date;
    private Long customerId;
    private Long accountId;
    private String accountNumber;
    private String accountType;

    private BigDecimal openingBalance;   // saldo antes del movimiento
    private Boolean status;

    private BigDecimal movementAmount;   // valor movimiento
    private String movementType;         // DEBIT / CREDIT
    private BigDecimal availableBalance; // saldo resultante
}