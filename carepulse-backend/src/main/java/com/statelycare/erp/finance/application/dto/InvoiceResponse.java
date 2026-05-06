package com.statelycare.erp.finance.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceResponse(
    UUID id,
    UUID residentId,
    BigDecimal amount,
    LocalDate issueDate,
    LocalDate dueDate,
    String status,
    String description
) {}
