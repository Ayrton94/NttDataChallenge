package com.bank.accounts.infrastructure.adapter.in.web;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex, ServerWebExchange exchange) {

        String msg = ex.getMessage() == null ? "BUSINESS_RULE" : ex.getMessage();

        HttpStatus status = switch (msg) {
            case "ACCOUNT_NUMBER_ALREADY_EXISTS" -> HttpStatus.CONFLICT;   // 409
            case "ACCOUNT_NOT_FOUND" -> HttpStatus.NOT_FOUND;              // 404
            default -> HttpStatus.CONFLICT;                                // BAD_REQUEST
        };

        ApiError body = ApiError.builder()
                .timestamp(Instant.now().toString())
                .path(exchange.getRequest().getPath().value())
                .code("BUSINESS_RULE")
                .error(msg)
                .build();

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleAny(Throwable ex, ServerWebExchange exchange) {
        log.error("Unhandled error", ex);

        ApiError body = ApiError.builder()
                .timestamp(Instant.now().toString())
                .path(exchange.getRequest().getPath().value())
                .code("INTERNAL_ERROR")
                .error("Internal Server Error")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @Data
    @Builder
    public static class ApiError {
        private String timestamp;
        private String path;
        private String code;
        private String error;
    }
}