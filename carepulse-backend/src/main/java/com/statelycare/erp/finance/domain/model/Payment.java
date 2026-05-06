package com.statelycare.erp.finance.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Payment(
    UUID id,
    UUID invoiceId,
    UUID residentId,
    BigDecimal amountPaid,
    Instant paymentDate,
    String paymentMethod // CASH, TRANSFER, CREDIT_CARD
) {
    public Payment {
        if (invoiceId == null) throw new IllegalArgumentException("Invoice ID is required");
        if (residentId == null) throw new IllegalArgumentException("Resident ID is required");
        if (amountPaid == null || amountPaid.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount paid must be greater than zero");
        if (paymentMethod == null || paymentMethod.isBlank()) throw new IllegalArgumentException("Payment method is required");
    }

    public static Payment register(UUID invoiceId, UUID residentId, BigDecimal amountPaid, String paymentMethod) {
        return new Payment(UUID.randomUUID(), invoiceId, residentId, amountPaid, Instant.now(), paymentMethod);
    }
}
