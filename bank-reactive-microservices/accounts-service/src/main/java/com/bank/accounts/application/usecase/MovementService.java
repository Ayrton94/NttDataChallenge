package com.bank.accounts.application.usecase;

import com.bank.accounts.application.port.out.AccountRepositoryPort;
import com.bank.accounts.application.port.out.MovementRepositoryPort;
import com.bank.accounts.domain.exception.BusinessException;
import com.bank.accounts.domain.exception.NotFoundException;
import com.bank.accounts.domain.model.Account;
import com.bank.accounts.domain.model.Movement;
import com.bank.accounts.domain.model.MovementType;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MovementService {

    private final AccountRepositoryPort accountRepo;
    private final MovementRepositoryPort movementRepo;
    private final TransactionalOperator tx;

    public Mono<Movement> createMovement(Long accountId, MovementType type, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new BusinessException("MOVEMENT_AMOUNT_INVALID"));
        }

        return accountRepo.findById(accountId)
                .switchIfEmpty(Mono.error(new NotFoundException("ACCOUNT_NOT_FOUND")))
                .flatMap(acc -> applyMovement(acc, type, amount))
                .as(tx::transactional);
    }

    private Mono<Movement> applyMovement(Account acc, MovementType type, BigDecimal amount) {
        BigDecimal current = acc.getBalance() == null ? BigDecimal.ZERO : acc.getBalance();
        BigDecimal newBalance;

        if (type == MovementType.DEBIT) {
            newBalance = current.subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                return Mono.error(new BusinessException("INSUFFICIENT_FUNDS"));
            }
        } else {
            newBalance = current.add(amount);
        }

        // 1) Actualizar saldo cuenta
        Account updated = Account.builder()
                .id(acc.getId())
                .customerId(acc.getCustomerId())
                .accountNumber(acc.getAccountNumber())
                .type(acc.getType())
                .balance(newBalance)
                .status(acc.isStatus())
                .build();

        // 2) Registrar movimiento
        Movement movement = Movement.builder()
                .accountId(acc.getId())
                .customerId(acc.getCustomerId())
                .movementDate(Instant.now())
                .type(type)
                .amount(amount)
                .balance(newBalance)
                .status(true)
                .build();

        return accountRepo.save(updated)
                .then(movementRepo.save(movement));
    }

    public Mono<Movement> findById(Long id) {
        return movementRepo.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("MOVEMENT_NOT_FOUND")));
    }

    public Flux<Movement> findAll() {
        return movementRepo.findAll();
    }

    public Flux<Movement> findByAccountId(Long accountId) {
        return movementRepo.findByAccountId(accountId);
    }

    public Mono<Void> deleteById(Long id) {
        return movementRepo.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("MOVEMENT_NOT_FOUND")))
                .flatMap(m -> movementRepo.deleteById(id));
    }
}