package com.bank.accounts.application.port.in;

import com.bank.accounts.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountQueryPort {
    Mono<Account> findById(Long id);
    Flux<Account> findAll();
    Flux<Account> findByCustomerId(Long customerId);
}