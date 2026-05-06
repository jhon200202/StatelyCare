package com.statelycare.erp.employee.application.dto;

import com.statelycare.erp.employee.domain.model.Department;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeRequest(
    UUID userId,
    String firstName,
    String lastName,
    Department department,
    String roleTitle,
    LocalDate hireDate
) {}
