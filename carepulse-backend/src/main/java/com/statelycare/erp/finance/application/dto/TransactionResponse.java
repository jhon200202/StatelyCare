package com.statelycare.erp.finance.application.dto;

import com.statelycare.erp.finance.domain.model.PaymentMethod;
import com.statelycare.erp.finance.domain.model.TransactionCategory;
import com.statelycare.erp.finance.domain.model.TransactionStatus;
import com.statelycare.erp.finance.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(
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
    LocalDate transactionDate
) {}
