package com.bank.accounts.infrastructure.adapter.out.persistence;

import java.time.Instant;

import com.bank.accounts.application.port.out.AccountRepositoryPort;
import com.bank.accounts.domain.model.Account;
import com.bank.accounts.domain.model.AccountType;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountR2dbcRepository repository;

    @Override
    public Mono<Account> save(Account account) {
        AccountEntity entity = toEntity(account);

        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(Instant.now());
        }

        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<Account> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Flux<Account> findAll() {
        return repository.findAll().map(this::toDomain);
    }

    @Override
    public Flux<Account> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId).map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    private AccountEntity toEntity(Account a) {
        return AccountEntity.builder()
                .id(a.getId())
                .customerId(a.getCustomerId())
                .accountNumber(a.getAccountNumber())
                .type(a.getType() == null ? null : a.getType().name())
                .balance(a.getBalance())
                .status(a.isStatus())
                .createdAt(a.getCreatedAt())
                .build();
    }

    private Account toDomain(AccountEntity e) {
        return Account.builder()
                .id(e.getId())
                .customerId(e.getCustomerId())
                .accountNumber(e.getAccountNumber())
                .type(e.getType() == null ? null : AccountType.valueOf(e.getType()))
                .balance(e.getBalance())
                .status(e.isStatus())
                .createdAt(e.getCreatedAt())
                .build();
    }
}