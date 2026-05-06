package com.statelycare.erp.finance.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateInvoiceRequest(
    @NotNull(message = "Resident ID is required")
    UUID residentId,
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    BigDecimal amount,
    
    @NotNull(message = "Issue date is required")
    LocalDate issueDate,
    
    @NotNull(message = "Due date is required")
    LocalDate dueDate,
    
    @NotBlank(message = "Description is required")
    String description
) {}
