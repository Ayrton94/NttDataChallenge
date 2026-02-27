package com.bank.accounts.infrastructure.adapter.out.persistence;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table("movements")
public class MovementEntity {
    @Id
    private Long id;

    private Long accountId;
    private Long customerId;

    private Instant movementDate;
    private String type;

    private BigDecimal amount;
    private BigDecimal balance;

    private boolean status;
    private Instant createdAt;
}