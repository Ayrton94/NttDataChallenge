package com.bank.accounts.application.port.out;

import com.bank.accounts.domain.model.Movement;
import java.time.Instant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementRepositoryPort {
    Mono<Movement> save(Movement movement);
    Mono<Movement> findById(Long id);
    Flux<Movement> findAll();
    Flux<Movement> findByAccountId(Long accountId);
    Flux<Movement> findByCustomerIdAndDateRange(Long customerId, Instant start, Instant end);
    Mono<Void> deleteById(Long id);
    Flux<Movement> findByAccountIdAndMovementDateBetween(Long accountId, Instant start, Instant end);
}