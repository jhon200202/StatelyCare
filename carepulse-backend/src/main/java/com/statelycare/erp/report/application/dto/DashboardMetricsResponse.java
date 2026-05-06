package com.statelycare.erp.report.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardMetricsResponse(
    int totalResidents,
    int activeStaff,
    int availableRooms,
    BigDecimal monthlyRevenue,
    List<FinancialTrendDto> financialTrends
) {}
