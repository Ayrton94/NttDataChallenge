package com.bank.accounts.infrastructure.adapter.out.persistence;

import com.bank.accounts.application.port.out.MovementRepositoryPort;
import com.bank.accounts.domain.model.Movement;
import com.bank.accounts.domain.model.MovementType;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepositoryPort {

    private final MovementR2dbcRepository repo;

    @Override
    public Mono<Movement> save(Movement movement) {
        return repo.save(toEntity(movement)).map(this::toDomain);
    }

    @Override
    public Mono<Movement> findById(Long id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    public Flux<Movement> findAll() {
        return repo.findAll().map(this::toDomain);
    }

    @Override
    public Flux<Movement> findByAccountId(Long accountId) {
        return repo.findByAccountId(accountId).map(this::toDomain);
    }

    @Override
    public Flux<Movement> findByCustomerIdAndDateRange(Long customerId, Instant start, Instant end) {
        return repo.findByCustomerIdAndMovementDateBetween(customerId, start, end)
                .map(this::toDomain);
    }

    @Override
    public Flux<Movement> findByAccountIdAndMovementDateBetween(Long accountId, Instant start, Instant end) {
        return repo.findByAccountIdAndMovementDateBetween(accountId, start, end)
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repo.deleteById(id);
    }

    private MovementEntity toEntity(Movement m) {
        return MovementEntity.builder()
                .id(m.getId())
                .accountId(m.getAccountId())
                .customerId(m.getCustomerId())
                .movementDate(m.getMovementDate())
                .type(m.getType() == null ? null : m.getType().name())
                .amount(m.getAmount())
                .balance(m.getBalance())
                .status(m.isStatus())
                .build();
    }

    private Movement toDomain(MovementEntity e) {
        return Movement.builder()
                .id(e.getId())
                .accountId(e.getAccountId())
                .customerId(e.getCustomerId())
                .movementDate(e.getMovementDate())
                .type(e.getType() == null ? null : MovementType.valueOf(e.getType()))
                .amount(e.getAmount())
                .balance(e.getBalance())
                .status(e.isStatus())
                .build();
    }
}