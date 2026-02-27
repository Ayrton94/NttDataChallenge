package com.bank.accounts.infrastructure.adapter.in.web.dto;

import com.bank.accounts.domain.model.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateAccountRequest {

    @NotNull
    private Long customerId;

    @NotNull
    private AccountType type;

    @NotNull
    @PositiveOrZero
    private BigDecimal balance;

    @NotNull
    private Boolean status;
}