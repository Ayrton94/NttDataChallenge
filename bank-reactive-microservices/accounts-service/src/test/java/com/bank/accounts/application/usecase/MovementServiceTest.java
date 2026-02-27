package com.bank.accounts.application.usecase;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.bank.accounts.application.port.out.AccountRepositoryPort;
import com.bank.accounts.application.port.out.MovementRepositoryPort;
import com.bank.accounts.domain.exception.BusinessException;
import com.bank.accounts.domain.model.Account;
import com.bank.accounts.domain.model.MovementType;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MovementServiceTest {

    @Mock
    private AccountRepositoryPort accountRepo;

    @Mock
    private MovementRepositoryPort movementRepo;

    @Mock
    private TransactionalOperator tx;

    @InjectMocks
    private MovementService movementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(tx.transactional(ArgumentMatchers.<Mono<?>>any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldThrowErrorWhenAmountIsInvalid() {

        StepVerifier.create(
                        movementService.createMovement(
                                1L,
                                MovementType.CREDIT,
                                BigDecimal.ZERO
                        )
                )
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException &&
                                ex.getMessage().equals("MOVEMENT_AMOUNT_INVALID")
                )
                .verify();
    }

    @Test
    void shouldThrowErrorWhenInsufficientFunds() {

        Account account = Account.builder()
                .id(1L)
                .customerId(16L)
                .balance(BigDecimal.valueOf(100))
                .status(true)
                .build();

        when(accountRepo.findById(1L))
                .thenReturn(Mono.just(account));

        StepVerifier.create(
                        movementService.createMovement(
                                1L,
                                MovementType.DEBIT,
                                BigDecimal.valueOf(200)
                        )
                )
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException &&
                                ex.getMessage().equals("INSUFFICIENT_FUNDS")
                )
                .verify();
    }

    @Test
    void shouldCreateCreditMovementSuccessfully() {

        Account account = Account.builder()
                .id(1L)
                .customerId(16L)
                .balance(BigDecimal.valueOf(100))
                .status(true)
                .build();

        when(accountRepo.findById(1L))
                .thenReturn(Mono.just(account));

        when(accountRepo.save(any()))
                .thenReturn(Mono.just(account));

        when(movementRepo.save(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(
                        movementService.createMovement(
                                1L,
                                MovementType.CREDIT,
                                BigDecimal.valueOf(50)
                        )
                )
                .expectNextMatches(movement ->
                        movement.getBalance()
                                .compareTo(BigDecimal.valueOf(150)) == 0
                )
                .verifyComplete();
    }
}