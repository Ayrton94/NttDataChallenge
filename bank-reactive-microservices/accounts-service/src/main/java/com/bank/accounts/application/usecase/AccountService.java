package com.bank.accounts.application.usecase;

import com.bank.accounts.application.port.in.AccountCommandPort;
import com.bank.accounts.application.port.in.AccountQueryPort;
import com.bank.accounts.application.port.out.AccountRepositoryPort;
import com.bank.accounts.domain.model.Account;
import com.bank.accounts.infrastructure.adapter.out.kafka.AccountKafkaPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class AccountService implements AccountCommandPort, AccountQueryPort {

    private final AccountRepositoryPort repository;
    private final AccountKafkaPublisher publisher;

    @Override
    public Mono<Account> create(Account account) {
        return repository.existsByAccountNumber(account.getAccountNumber())
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalStateException("ACCOUNT_NUMBER_ALREADY_EXISTS"))
                        : repository.save(account)
                )
                .flatMap(saved ->
                        publisher.publishCreated(saved)
                                .onErrorResume(ex -> {
                                    log.error("Kafka publishCreated failed", ex);
                                    return Mono.empty();
                                })
                                .thenReturn(saved)
                );
    }

    @Override
    public Mono<Account> update(Long id, Account account) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalStateException("ACCOUNT_NOT_FOUND")))
                .flatMap(existing -> {
                    account.setId(existing.getId());
                    account.setAccountNumber(existing.getAccountNumber());
                    account.setCreatedAt(existing.getCreatedAt());
                    return repository.save(account);
                })
                .flatMap(saved ->
                        publisher.publishUpdated(saved)
                                .onErrorResume(ex -> {
                                    log.error("Kafka publishUpdated failed", ex);
                                    return Mono.empty();
                                })
                                .thenReturn(saved)
                );
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalStateException("ACCOUNT_NOT_FOUND")))
                .flatMap(existing ->
                        repository.deleteById(id)
                                .then(
                                        publisher.publishDeleted(existing.getId(), existing.getCustomerId())
                                                .onErrorResume(ex -> {
                                                    log.error("Kafka publishDeleted failed", ex);
                                                    return Mono.empty();
                                                })
                                )
                );
    }

    @Override
    public Mono<Account> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Flux<Account> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }
}