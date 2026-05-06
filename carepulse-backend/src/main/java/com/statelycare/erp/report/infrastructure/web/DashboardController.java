package com.statelycare.erp.report.infrastructure.web;

import com.statelycare.erp.report.application.dto.DashboardMetricsResponse;
import com.statelycare.erp.report.application.usecase.GetDashboardMetricsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final GetDashboardMetricsUseCase getDashboardMetricsUseCase;

    public DashboardController(GetDashboardMetricsUseCase getDashboardMetricsUseCase) {
        this.getDashboardMetricsUseCase = getDashboardMetricsUseCase;
    }

    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetricsResponse> getMetrics() {
        return ResponseEntity.ok(getDashboardMetricsUseCase.execute());
    }
}
