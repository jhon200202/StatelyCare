package com.statelycare.erp.finance.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record Invoice(
    UUID id,
    UUID residentId,
    BigDecimal amount,
    LocalDate issueDate,
    LocalDate dueDate,
    String status, // PENDING, PAID, OVERDUE
    String description,
    Instant createdAt,
    Instant updatedAt
) {
    public Invoice {
        if (residentId == null) throw new IllegalArgumentException("Resident ID is required");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be greater than zero");
        if (issueDate == null) throw new IllegalArgumentException("Issue date is required");
        if (dueDate == null) throw new IllegalArgumentException("Due date is required");
        if (status == null || status.isBlank()) throw new IllegalArgumentException("Status is required");
    }

    public static Invoice createNew(UUID residentId, BigDecimal amount, LocalDate issueDate, LocalDate dueDate, String description) {
        Instant now = Instant.now();
        return new Invoice(UUID.randomUUID(), residentId, amount, issueDate, dueDate, "PENDING", description, now, now);
    }

    public Invoice markAsPaid() {
        return new Invoice(this.id, this.residentId, this.amount, this.issueDate, this.dueDate, "PAID", this.description, this.createdAt, Instant.now());
    }

    public Invoice markAsOverdue() {
        return new Invoice(this.id, this.residentId, this.amount, this.issueDate, this.dueDate, "OVERDUE", this.description, this.createdAt, Instant.now());
    }
}
