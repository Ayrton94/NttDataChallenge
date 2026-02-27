package com.bank.accounts.infrastructure.adapter.in.web;

import com.bank.accounts.application.usecase.AccountService;
import com.bank.accounts.domain.model.Account;
import com.bank.accounts.infrastructure.adapter.in.web.dto.AccountResponse;
import com.bank.accounts.infrastructure.adapter.in.web.dto.CreateAccountRequest;
import com.bank.accounts.infrastructure.adapter.in.web.dto.UpdateAccountRequest;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountResponse> create(@Valid @RequestBody CreateAccountRequest req) {
        Account account = Account.builder()
                .customerId(req.getCustomerId())
                .accountNumber(req.getAccountNumber())
                .type(req.getType())
                .balance(req.getBalance())
                .status(Boolean.TRUE.equals(req.getStatus()))
                .createdAt(Instant.now())
                .build();

        return service.create(account).map(this::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<AccountResponse> findById(@PathVariable Long id) {
        return service.findById(id).map(this::toResponse);
    }

    @GetMapping
    public Flux<AccountResponse> findAll(@RequestParam(required = false) Long customerId) {
        if (customerId != null) {
            return service.findByCustomerId(customerId).map(this::toResponse);
        }
        return service.findAll().map(this::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<AccountResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateAccountRequest req) {
        Account account = Account.builder()
                .customerId(req.getCustomerId())
                .type(req.getType())
                .balance(req.getBalance())
                .status(Boolean.TRUE.equals(req.getStatus()))
                .build();

        return service.update(id, account).map(this::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    private AccountResponse toResponse(Account a) {
        return AccountResponse.builder()
                .id(a.getId())
                .customerId(a.getCustomerId())
                .accountNumber(a.getAccountNumber())
                .type(a.getType())
                .balance(a.getBalance())
                .status(a.isStatus())
                .createdAt(a.getCreatedAt())
                .build();
    }
}