package com.statelycare.erp.finance.application.dto;

import com.statelycare.erp.finance.domain.model.PaymentMethod;
import com.statelycare.erp.finance.domain.model.TransactionCategory;
import com.statelycare.erp.finance.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequest(
    TransactionType transactionType,
    TransactionCategory category,
    BigDecimal amount,
    String description,
    UUID residentId,
    String vendorName,
    String invoiceNumber,
    PaymentMethod paymentMethod,
    LocalDate transactionDate
) {}
