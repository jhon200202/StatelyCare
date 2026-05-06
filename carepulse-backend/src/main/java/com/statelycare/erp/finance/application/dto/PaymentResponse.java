package com.statelycare.erp.finance.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
    UUID id,
    UUID invoiceId,
    UUID residentId,
    BigDecimal amountPaid,
    Instant paymentDate,
    String paymentMethod
) {}
