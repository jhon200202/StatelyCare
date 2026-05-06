package com.statelycare.erp.finance.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record FinancialTransaction(
    UUID id,
    String transactionCode,
    TransactionType transactionType,
    TransactionCategory category,
    BigDecimal amount,
    String currency,
    String description,
    UUID residentId,
    String vendorName,
    String invoiceNumber,
    PaymentMethod paymentMethod,
    TransactionStatus status,
    LocalDate transactionDate,
    LocalDate dueDate,
    LocalDate settledDate,
    UUID createdBy,
    Instant createdAt,
    Instant updatedAt
) {
    public static FinancialTransaction createNew(
            String transactionCode, 
            TransactionType type, 
            TransactionCategory category, 
            BigDecimal amount, 
            PaymentMethod method, 
            LocalDate date) {
        Instant now = Instant.now();
        return new FinancialTransaction(
            UUID.randomUUID(),
            transactionCode,
            type,
            category,
            amount,
            "USD",
            null,
            null,
            null,
            null,
            method,
            TransactionStatus.PENDING,
            date,
            null,
            null,
            null,
            now,
            now
        );
    }
}
