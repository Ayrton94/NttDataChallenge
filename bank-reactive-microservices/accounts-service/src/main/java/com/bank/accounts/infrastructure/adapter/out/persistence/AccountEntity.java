package com.bank.accounts.infrastructure.adapter.out.persistence;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("accounts")
public class AccountEntity {

    @Id
    private Long id;

    @Column("customer_id")
    private Long customerId;

    @Column("account_number")
    private String accountNumber;

    @Column("type")
    private String type; // SAVINGS | CHECKING

    @Column("balance")
    private BigDecimal balance;

    @Column("status")
    private boolean status;

    @Column("created_at")
    private Instant createdAt;
}