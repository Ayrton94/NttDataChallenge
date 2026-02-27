package com.bank.accounts.infrastructure.config;

import com.bank.accounts.application.port.out.AccountRepositoryPort;
import com.bank.accounts.application.port.out.MovementRepositoryPort;
import com.bank.accounts.application.usecase.AccountService;
import com.bank.accounts.application.usecase.MovementService;
import com.bank.accounts.application.usecase.ReportService;
import com.bank.accounts.infrastructure.adapter.out.kafka.AccountKafkaPublisher;
import com.bank.accounts.infrastructure.adapter.out.persistence.AccountR2dbcRepository;
import com.bank.accounts.infrastructure.adapter.out.persistence.AccountRepositoryAdapter;
import com.bank.accounts.infrastructure.adapter.out.persistence.MovementR2dbcRepository;
import com.bank.accounts.infrastructure.adapter.out.persistence.MovementRepositoryAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
public class BeansConfig {

    // 1) HEXAGONAL: Adapter out (persistencia) expuesto como Port
    @Bean
    public AccountRepositoryPort accountRepositoryPort(AccountR2dbcRepository r2dbcRepo) {
        return new AccountRepositoryAdapter(r2dbcRepo);
    }

    // 2) ObjectMapper con soporte JavaTime (Instant) para Kafka
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    // 3) ProducerFactory y KafkaTemplate (sin KafkaProperties) para evitar errores de imports
    @Bean
    public ProducerFactory<String, Object> producerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.producer.acks:all}") String acks,
            @Value("${spring.kafka.producer.retries:3}") int retries,
            ObjectMapper objectMapper
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        JsonSerializer<Object> valueSerializer = new JsonSerializer<>(objectMapper);
        valueSerializer.setAddTypeInfo(false); // sin type headers

        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), valueSerializer);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    // 4) Publisher
    @Bean
    public AccountKafkaPublisher accountKafkaPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.accounts-events}") String topicName
    ) {
        return new AccountKafkaPublisher(kafkaTemplate, topicName);
    }

    // 5) UseCase
    @Bean
    public AccountService accountService(AccountRepositoryPort repoPort, AccountKafkaPublisher publisher) {
        return new AccountService(repoPort, publisher);
    }

    @Bean
    public MovementRepositoryPort movementRepositoryPort(MovementR2dbcRepository repo) {
        return new MovementRepositoryAdapter(repo);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager txm) {
        return TransactionalOperator.create(txm);
    }

    @Bean
    public MovementService movementService(
            AccountRepositoryPort accountRepo,
            MovementRepositoryPort movementRepo,
            TransactionalOperator tx
    ) {
        return new MovementService(accountRepo, movementRepo, tx);
    }

    @Bean
    public ReportService reportService(AccountRepositoryPort aRepo, MovementRepositoryPort mRepo) {
        return new ReportService(aRepo, mRepo);
    }
}
