package com.bank.accounts.application.port.in;

import com.bank.accounts.domain.model.Account;
import reactor.core.publisher.Mono;

public interface AccountCommandPort {
    Mono<Account> create(Account account);
    Mono<Account> update(Long id, Account account);
    Mono<Void> delete(Long id);
}