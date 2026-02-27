package com.bank.accounts.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateMovementRequest {
    @NotNull
    private Long accountId;

    @NotNull
    private String type; // DEBIT/CREDIT

    @NotNull
    @Positive
    private BigDecimal amount;
}