package com.bank.accounts.application.usecase;

import com.bank.accounts.application.port.out.AccountRepositoryPort;
import com.bank.accounts.application.port.out.MovementRepositoryPort;
import com.bank.accounts.domain.model.Account;
import com.bank.accounts.domain.model.Movement;
import com.bank.accounts.domain.model.MovementType;
import com.bank.accounts.infrastructure.adapter.in.web.dto.MovementReportRow;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportService {

    private final AccountRepositoryPort accountRepo;
    private final MovementRepositoryPort movementRepo;

    public Flux<MovementReportRow> reportByCustomerAndDates(Long customerId, Instant start, Instant end) {
        Mono<Map<Long, Account>> accountsByIdMono =
                accountRepo.findByCustomerId(customerId)
                        .collectMap(Account::getId, Function.identity());

        return accountsByIdMono.flatMapMany(accountsById ->
                movementRepo.findByCustomerIdAndDateRange(customerId, start, end)
                        .map(m -> toRow(m, accountsById.get(m.getAccountId())))
        );
    }

    private MovementReportRow toRow(Movement m, Account acc) {
        BigDecimal after = m.getBalance() == null ? BigDecimal.ZERO : m.getBalance();
        BigDecimal amount = m.getAmount() == null ? BigDecimal.ZERO : m.getAmount();

        BigDecimal before;
        if (m.getType() == MovementType.DEBIT) {
            before = after.add(amount);
        } else {
            before = after.subtract(amount);
        }

        return MovementReportRow.builder()
                .date(m.getMovementDate())
                .customerId(m.getCustomerId())
                .accountId(m.getAccountId())
                .accountNumber(acc != null ? acc.getAccountNumber() : null)
                .accountType(acc != null && acc.getType() != null ? acc.getType().name() : null)
                .openingBalance(before)
                .status(m.isStatus())
                .movementAmount(amount)
                .movementType(m.getType() != null ? m.getType().name() : null)
                .availableBalance(after)
                .build();
    }
}