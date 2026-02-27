package com.bank.accounts.integration;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bank.accounts.infrastructure.adapter.in.web.dto.CreateMovementRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovementIntegrationTest {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldCreateMovementAndReturnItInReport() {

        // Crear movimiento
        CreateMovementRequest request = new CreateMovementRequest();
        request.setAccountId(18L);
        request.setType("CREDIT");
        request.setAmount(BigDecimal.valueOf(100));

        webTestClient.post()
                .uri("/api/v1/movements")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        // Consultar reporte
        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/reports/{customerId}")
                                .queryParam("startDate", "2026-01-01T00:00:00Z")
                                .queryParam("endDate", "2026-12-31T23:59:59Z")
                                .build(16)
                )
                .exchange()
                .expectStatus().isOk();
    }
}