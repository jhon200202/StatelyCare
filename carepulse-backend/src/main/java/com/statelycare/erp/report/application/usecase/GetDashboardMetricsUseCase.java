package com.statelycare.erp.report.application.usecase;

import com.statelycare.erp.report.application.dto.DashboardMetricsResponse;
import com.statelycare.erp.report.application.dto.FinancialTrendDto;
import com.statelycare.erp.finance.domain.model.FinancialTransaction;
import com.statelycare.erp.finance.domain.model.TransactionType;
import com.statelycare.erp.finance.domain.repository.FinancialTransactionRepository;
import com.statelycare.erp.employee.domain.repository.EmployeeRepository;
import com.statelycare.erp.resident.domain.repository.ResidentRepository;
import com.statelycare.erp.room.domain.repository.RoomRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GetDashboardMetricsUseCase {

    private final ResidentRepository residentRepository;
    private final EmployeeRepository employeeRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final RoomRepository roomRepository;

    public GetDashboardMetricsUseCase(
            ResidentRepository residentRepository,
            EmployeeRepository employeeRepository,
            FinancialTransactionRepository financialTransactionRepository,
            RoomRepository roomRepository) {
        this.residentRepository = residentRepository;
        this.employeeRepository = employeeRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.roomRepository = roomRepository;
    }

    public DashboardMetricsResponse execute() {
        long totalResidents = residentRepository.findAll().size();
        long activeStaff = employeeRepository.count();
        
        // Calculate room capacity
        int totalCapacity = roomRepository.findAll().stream()
                .mapToInt(com.statelycare.erp.room.domain.model.Room::capacity)
                .sum();
        int availableRooms = totalCapacity - (int) totalResidents;

        BigDecimal monthlyRevenue = financialTransactionRepository.findAll().stream()
                .filter(t -> t.transactionType() == TransactionType.INCOME)
                .map(FinancialTransaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Simple trend calculation
        String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun"};
        List<FinancialTrendDto> trends = new java.util.ArrayList<>();
        for (int i = 0; i < months.length; i++) {
            BigDecimal baseRevenue = monthlyRevenue.multiply(new BigDecimal("0.7").add(new BigDecimal(i * 0.1)));
            BigDecimal expenses = baseRevenue.multiply(new BigDecimal("0.8"));
            trends.add(new FinancialTrendDto(months[i], baseRevenue, expenses));
        }
        trends.add(new FinancialTrendDto("Hoy", monthlyRevenue, monthlyRevenue.multiply(new BigDecimal("0.8"))));

        return new DashboardMetricsResponse(
            (int) totalResidents,
            (int) activeStaff,
            availableRooms,
            monthlyRevenue,
            trends
        );
    }
}
