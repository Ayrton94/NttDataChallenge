package com.bank.accounts.infrastructure.adapter.out.kafka.event;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountEvent {
    private AccountEventType eventType;
    private Long accountId;
    private Long customerId;
    private String accountNumber;
    private String type;
    private BigDecimal balance;
    private boolean status;
    // private Instant occurredAt;
    private String occurredAt;
}