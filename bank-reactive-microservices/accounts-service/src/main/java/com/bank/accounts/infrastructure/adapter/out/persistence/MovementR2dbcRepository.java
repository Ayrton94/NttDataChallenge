package com.bank.accounts.infrastructure.adapter.out.persistence;

import java.time.Instant;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MovementR2dbcRepository extends ReactiveCrudRepository<MovementEntity, Long> {
    Flux<MovementEntity> findByAccountId(Long accountId);

    @Query("""
        SELECT * FROM movements
        WHERE customer_id = :customerId
          AND movement_date BETWEEN :from AND :to
        ORDER BY movement_date DESC
    """)
    Flux<MovementEntity> findByCustomerIdAndDateRange(Long customerId, Instant from, Instant to);
    Flux<MovementEntity> findByCustomerIdAndMovementDateBetween(Long customerId, Instant start, Instant end);
    Flux<MovementEntity> findByAccountIdAndMovementDateBetween(Long accountId, Instant start, Instant end);

}