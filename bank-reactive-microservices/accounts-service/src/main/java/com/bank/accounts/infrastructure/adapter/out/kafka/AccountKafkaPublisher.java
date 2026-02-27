package com.bank.accounts.infrastructure.adapter.out.kafka;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;

import com.bank.accounts.domain.model.Account;
import com.bank.accounts.infrastructure.adapter.out.kafka.event.AccountEvent;
import com.bank.accounts.infrastructure.adapter.out.kafka.event.AccountEventType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class AccountKafkaPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topicName;

    public Mono<Void> publishCreated(Account a) {
        return send(AccountEventType.ACCOUNT_CREATED, a);
    }

    public Mono<Void> publishUpdated(Account a) {
        return send(AccountEventType.ACCOUNT_UPDATED, a);
    }

    public Mono<Void> publishDeleted(Long accountId, Long customerId) {
        AccountEvent event = AccountEvent.builder()
                .eventType(AccountEventType.ACCOUNT_DELETED)
                .accountId(accountId)
                .customerId(customerId)
                .occurredAt(Instant.now().toString())
                .build();

        return Mono.fromFuture(kafkaTemplate.send(topicName, String.valueOf(accountId), event))
                .doOnSuccess(r -> log.info("Kafka OK -> {} key={}", event.getEventType(), accountId))
                .doOnError(ex -> log.error("Kafka FAIL -> ACCOUNT_DELETED key={}", accountId, ex))
                .then();
    }

    private Mono<Void> send(AccountEventType type, Account a) {
        AccountEvent event = AccountEvent.builder()
                .eventType(type)
                .accountId(a.getId())
                .customerId(a.getCustomerId())
                .accountNumber(a.getAccountNumber())
                .type(a.getType() == null ? null : a.getType().name())
                .balance(a.getBalance())
                .status(a.isStatus())
                .occurredAt(Instant.now().toString())
                .build();

        return Mono.fromFuture(kafkaTemplate.send(topicName, String.valueOf(a.getId()), event))
                .doOnSuccess(r -> log.info("Kafka OK -> {} key={}", type, a.getId()))
                .doOnError(ex -> log.error("Kafka FAIL -> {} key={}", type, a.getId(), ex))
                .then();
    }
}