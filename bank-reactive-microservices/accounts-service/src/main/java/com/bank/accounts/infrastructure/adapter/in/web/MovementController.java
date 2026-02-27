package com.bank.accounts.infrastructure.adapter.in.web;

import com.bank.accounts.application.usecase.MovementService;
import com.bank.accounts.domain.model.Movement;
import com.bank.accounts.domain.model.MovementType;
import com.bank.accounts.infrastructure.adapter.in.web.dto.CreateMovementRequest;
import com.bank.accounts.infrastructure.adapter.in.web.dto.MovementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovementResponse> create(@Valid @RequestBody CreateMovementRequest req) {
        MovementType type = MovementType.valueOf(req.getType().toUpperCase());
        return service.createMovement(req.getAccountId(), type, req.getAmount())
                .map(this::toResponse);
    }

    @GetMapping
    public Flux<MovementResponse> findAll() {
        return service.findAll().map(this::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<MovementResponse> findById(@PathVariable Long id) {
        return service.findById(id).map(this::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return service.deleteById(id);
    }

    private MovementResponse toResponse(Movement m) {
        return MovementResponse.builder()
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
}