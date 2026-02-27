package com.bank.accounts.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private Long customerId;
    private String accountNumber;
    private AccountType type;
    private BigDecimal balance;
    private boolean status;
    private Instant createdAt;
}