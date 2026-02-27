package com.bank.accounts.domain.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}