package com.bank.accounts.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountR2dbcRepository extends ReactiveCrudRepository<AccountEntity, Long> {

    Flux<AccountEntity> findByCustomerId(Long customerId);

    Mono<Boolean> existsByAccountNumber(String accountNumber);
}