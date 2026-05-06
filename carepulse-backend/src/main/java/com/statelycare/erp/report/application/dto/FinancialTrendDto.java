package com.statelycare.erp.report.application.dto;

import java.math.BigDecimal;

public record FinancialTrendDto(
    String month,
    BigDecimal revenue,
    BigDecimal expenses
) {}
