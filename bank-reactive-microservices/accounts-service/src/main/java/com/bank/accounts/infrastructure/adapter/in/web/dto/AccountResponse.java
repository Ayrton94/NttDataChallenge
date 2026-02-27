package com.bank.accounts.infrastructure.adapter.in.web.dto;

import com.bank.accounts.domain.model.AccountType;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private Long customerId;
    private String accountNumber;
    private AccountType type;
    private BigDecimal balance;
    private boolean status;
    private Instant createdAt;
}