package com.bank.accounts.application.port.out;

import com.bank.accounts.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepositoryPort {
    Mono<Account> save(Account account);
    Mono<Account> findById(Long id);
    Flux<Account> findAll();
    Flux<Account> findByCustomerId(Long customerId);
    Mono<Void> deleteById(Long id);
    Mono<Boolean> existsByAccountNumber(String accountNumber);
}